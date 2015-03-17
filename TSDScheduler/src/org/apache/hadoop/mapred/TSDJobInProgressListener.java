package org.apache.hadoop.mapred;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobStatusChangeEvent.EventType;
import org.apache.hadoop.mapred.TSDJobInProgressListener.JobSchedulingInfo;
import org.apache.hadoop.mapreduce.Job;


public class TSDJobInProgressListener extends JobInProgressListener {

  /**
   * A class that groups all the information from a {@link JobInProgress} that
   * is necessary for scheduling a job.
   */
  static class JobSchedulingInfo {
    private JobPriority priority;
    private long startTime;
    private JobID id;
    private long deadLine;//截止时间
    private boolean isCPU;
    public JobInProgress jip;

    public JobSchedulingInfo(JobInProgress jip) {


      this(jip.getStatus(), jip);


    }

    public JobSchedulingInfo(JobStatus status, JobInProgress jip) {
      Configuration conf = jip.getJobConf();
      priority = status.getJobPriority();
      startTime = status.getStartTime();
      id = status.getJobID();
      isCPU = false;

      if (conf.getLong("user.deadline", Long.MAX_VALUE) == Long.MAX_VALUE) {
        deadLine = Long.MAX_VALUE;
      } else {
        deadLine = startTime + conf.getLong("user.deadline", 0) * 1000;
      }
      isCPU = conf.getBoolean("isCPU", false);
      this.jip = jip;


    }


    JobPriority getPriority() {
      return priority;
    }

    long getStartTime() {
      return startTime;
    }

    JobID getJobID() {
      return id;
    }

    long getDeadLine() {
      return deadLine;
    }

    public boolean isCPU() {
      return isCPU;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != JobSchedulingInfo.class) {
        return false;
      } else if (obj == this) {
        return true;
      } else if (obj instanceof JobSchedulingInfo) {
        JobSchedulingInfo that = (JobSchedulingInfo) obj;
        return (this.id.equals(that.id) &&
            this.startTime == that.startTime &&
            this.priority == that.priority &&
            this.deadLine == that.deadLine &&
            this.isCPU == that.isCPU);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return (int) (id.hashCode() * priority.hashCode() + startTime);
    }

  }

  static final Comparator<JobSchedulingInfo> TSD_JOB_QUEUE_COMPARATOR
      = new Comparator<JobSchedulingInfo>() {
    //o1的优先级若高于o2，则返回负数
    public int compare(JobSchedulingInfo o1, JobSchedulingInfo o2) {
            /*
	    	 * 截止时间越短，优先级越高 
	    	 */
      if (o1.getDeadLine() - o2.getDeadLine() < 0) {
        return -1;
      } else if (o1.getDeadLine() - o2.getDeadLine() > 0) {
        return 1;
      }
      //FIFO调度器
      int res = o1.getPriority().compareTo(o2.getPriority());
      if (res == 0) {
        if (o1.getStartTime() < o2.getStartTime()) {
          res = -1;
        } else {
          res = (o1.getStartTime() == o2.getStartTime() ? 0 : 1);
        }
      }
      if (res == 0) {
        res = o1.getJobID().compareTo(o2.getJobID());
      }
      return res;
    }
  };

  private Map<JobSchedulingInfo, JobInProgress> IOJobQueue;
  private Map<JobSchedulingInfo, JobInProgress> CPUJobQueue;


  public TSDJobInProgressListener() {
    this(new TreeMap<JobSchedulingInfo,
        JobInProgress>(TSD_JOB_QUEUE_COMPARATOR));
  }

  /**
   * For clients that want to provide their own job priorities.
   *
   * @param jobQueue A collection whose iterator returns jobs in priority order.
   */
  protected TSDJobInProgressListener(Map<JobSchedulingInfo,
      JobInProgress> jobQueue) {
    this.IOJobQueue = Collections.synchronizedMap(jobQueue);
    this.CPUJobQueue = Collections.synchronizedMap(jobQueue);
  }

  /**
   * Returns a synchronized view of the job queue.
   */
  public Collection<JobInProgress> getJobQueue(boolean isCPU) {
    if (isCPU)
      return CPUJobQueue.values();
    else return IOJobQueue.values();
  }

  /**
   * Returns a synchronized view of the job queue.
   */
  public Collection<JobInProgress> getBothJobQueue() {
    Map<JobSchedulingInfo, JobInProgress> newQueue = new TreeMap<JobSchedulingInfo, JobInProgress>(
        TSD_JOB_QUEUE_COMPARATOR);
    synchronized (CPUJobQueue) {
      synchronized (IOJobQueue) {

        newQueue.putAll(CPUJobQueue);
        newQueue.putAll(IOJobQueue);

      }
    }
    return newQueue.values();

  }

  public Map<JobSchedulingInfo, JobInProgress> getJobs(boolean isCPU) {
    if (isCPU && !CPUJobQueue.isEmpty())
      return CPUJobQueue;
    else return IOJobQueue;
  }

  @Override
  public void jobAdded(JobInProgress job) {
    synchronized (CPUJobQueue) {
      synchronized (IOJobQueue) {
        boolean isCPU = job.getJobConf().getBoolean("isCPU", false);
        if (isCPU)
          CPUJobQueue.put(new JobSchedulingInfo(job), job);
        else
          IOJobQueue.put(new JobSchedulingInfo(job), job);
      }


    }
  }

  // Job will be removed once the job completes
  @Override
  public void jobRemoved(JobInProgress job) {
  }

  private void jobCompleted(JobSchedulingInfo oldInfo) {
    //jobQueue.remove(oldInfo);
    this.reOrderJobs(oldInfo.isCPU());
  }

  @Override
  public synchronized void jobUpdated(JobChangeEvent event) {
    JobInProgress job = event.getJobInProgress();
    if (event instanceof JobStatusChangeEvent) {
      // Check if the ordering of the job has changed
      // For now priority and start-time can change the job ordering
      JobStatusChangeEvent statusEvent = (JobStatusChangeEvent) event;
      JobSchedulingInfo oldInfo =
          new JobSchedulingInfo(statusEvent.getOldStatus(), job);
      if (statusEvent.getEventType() == EventType.PRIORITY_CHANGED
          || statusEvent.getEventType() == EventType.START_TIME_CHANGED) {
        // Make a priority change
        reorderJobs(job, oldInfo);
      } else if (statusEvent.getEventType() == EventType.RUN_STATE_CHANGED) {
        // Check if the job is complete
        int runState = statusEvent.getNewStatus().getRunState();
        if (runState == JobStatus.SUCCEEDED
            || runState == JobStatus.FAILED
            || runState == JobStatus.KILLED) {
          jobCompleted(oldInfo);
        }
      }
    }
  }

  private void reorderJobs(JobInProgress job, JobSchedulingInfo oldInfo) {
    if (oldInfo.isCPU) {
      synchronized (CPUJobQueue) {
        CPUJobQueue.remove(oldInfo);
        CPUJobQueue.put(new JobSchedulingInfo(job), job);
      }
    } else {
      synchronized (IOJobQueue) {
        IOJobQueue.remove(oldInfo);
        IOJobQueue.put(new JobSchedulingInfo(job), job);
      }
    }
  }

  public synchronized void reOrderJobs(boolean isCPU) {
    if (isCPU) {
      synchronized (CPUJobQueue) {
        // 插入到newQueue,排序
        Map<JobSchedulingInfo, JobInProgress> newQueue = new TreeMap<JobSchedulingInfo, JobInProgress>(
            TSD_JOB_QUEUE_COMPARATOR);
        Iterator itrKey = CPUJobQueue.keySet().iterator();
        while (itrKey.hasNext()) {
          JobSchedulingInfo info = (JobSchedulingInfo) itrKey.next();
          if (info.jip.isComplete()) {
            continue;
          }
          newQueue.put(info, info.jip);
        }
        CPUJobQueue = newQueue;
      }
    } else {
      synchronized (IOJobQueue) {
        // 插入到newQueue,排序
        Map<JobSchedulingInfo, JobInProgress> newQueue = new TreeMap<JobSchedulingInfo, JobInProgress>(
            TSD_JOB_QUEUE_COMPARATOR);
        Iterator itrKey = IOJobQueue.keySet().iterator();
        while (itrKey.hasNext()) {
          JobSchedulingInfo info = (JobSchedulingInfo) itrKey.next();
          if (info.jip.isComplete()) {
            continue;
          }
          newQueue.put(info, info.jip);
        }
        IOJobQueue = newQueue;
      }
    }
  }
}
