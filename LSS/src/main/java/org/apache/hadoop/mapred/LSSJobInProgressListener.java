package org.apache.hadoop.mapred;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobStatusChangeEvent.EventType;
import org.apache.hadoop.mapreduce.Job;

public class LSSJobInProgressListener extends JobInProgressListener {
  /**
   * 集群的状态
   */
  public static int NUMMapSlots = 10;// 集群的Map Slots数目
  public static int NUMReduceSlots = 5;// 集群的Reduce Slots数目

  /**
   * A class that groups all the information from a {@link JobInProgress} that
   * is necessary for scheduling a job.
   */
  static class JobSchedulingInfo {
    private JobPriority priority;
    private long startTime;
    private JobID id;
    private long deadLine;// 截止时间
    private double spareTime = 0;// 空闲时间
    public JobInProgress jip;
    private double AvgTime = 0;// task平均执行时间

    public JobSchedulingInfo(JobInProgress jip) {
      this(jip.getStatus(), jip);
    }

    public JobSchedulingInfo(JobStatus status, JobInProgress jip) {

      Configuration conf = jip.getJobConf();
      priority = status.getJobPriority();
      startTime = status.getStartTime();
      id = status.getJobID();

      if (conf.getLong("user.deadline", Long.MAX_VALUE) == Long.MAX_VALUE) {
        deadLine = Long.MAX_VALUE;
      } else {
        deadLine = startTime + conf.getLong("user.deadline", 0) * 1000;
      }
      spareTime = deadLine - System.currentTimeMillis();
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

    // 获取空闲时间
    double getSpareTime() {
      return spareTime;
    }

    // 设置空闲时间
    void setSpareTime(double t) {
      spareTime = t;
    }

    // 获取平均时间
    double getAvgTime() {
      return AvgTime;
    }

    void setAvgTime(double avg) {
      AvgTime = avg;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != JobSchedulingInfo.class) {
        return false;
      } else if (obj == this) {
        return true;
      } else if (obj instanceof JobSchedulingInfo) {
        JobSchedulingInfo that = (JobSchedulingInfo) obj;
        return (this.id.equals(that.id)
            && this.startTime == that.startTime && this.priority == that.priority);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return (int) (id.hashCode() * priority.hashCode() + startTime);
    }

  }

  static final Comparator<JobSchedulingInfo> LLF_JOB_QUEUE_COMPARATOR = new Comparator<JobSchedulingInfo>() {
    // 若o1的优先级高于o2，则返回负数
    public int compare(JobSchedulingInfo o1, JobSchedulingInfo o2) {
      /**
       * 空闲时间>0,空闲时间越短，优先级越高
       * 若存在job的空闲时间<0,空闲时间越长，优先级越高
       */
      double d1 = o1.getSpareTime();
      double d2 = o2.getSpareTime();
      if (d1 >= 0 && d2 >= 0) {
        if (d1 - d2 < 0) {
          return -1;
        } else if (d1 - d2 > 0) {
          return 1;
        }
      }
      if (d1 <= 0 || d2 <= 0) {
        if (d1 - d2 < 0) {
          return 1;
        } else if (d1 - d2 > 0) {
          return -1;
        }
      }

      // FIFO调度器
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

  private Map<JobSchedulingInfo, JobInProgress> jobQueue;

  public LSSJobInProgressListener() {
    this(new TreeMap<JobSchedulingInfo, JobInProgress>(
        LLF_JOB_QUEUE_COMPARATOR));
  }

  /**
   * For clients that want to provide their own job priorities.
   *
   * @param jobQueue A collection whose iterator returns jobs in priority order.
   */
  protected LSSJobInProgressListener(
      Map<JobSchedulingInfo, JobInProgress> jobQueue) {
    this.jobQueue = Collections.synchronizedMap(jobQueue);
  }

  /**
   * Returns a synchronized view of the job queue.
   */
  public Collection<JobInProgress> getJobQueue() {
    return jobQueue.values();
  }

  public Map<JobSchedulingInfo, JobInProgress> getJobs() {
    return jobQueue;
  }

  @Override
  public void jobAdded(JobInProgress job) {
    synchronized (jobQueue) {
      jobQueue.put(new JobSchedulingInfo(job), job);
    }
  }

  // Job will be removed once the job completes
  @Override
  public void jobRemoved(JobInProgress job) {
  }

  private void jobCompleted(JobSchedulingInfo oldInfo) {
    synchronized (jobQueue) {
      jobQueue.remove(oldInfo);
    }
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
    synchronized (jobQueue) {
      jobQueue.remove(oldInfo);
      jobQueue.put(new JobSchedulingInfo(job), job);
    }
  }

  /**
   * 根据JobInProgress获取相应的JobSchedulingInfo
   */
  public JobSchedulingInfo getJobSchedulingInfo(JobInProgress job) {
    Map<JobSchedulingInfo, JobInProgress> map = jobQueue;

    for(JobSchedulingInfo info: map.keySet()){
      JobInProgress value =  map.get(info);
      if (value.getJobID().equals(job.getJobID()))
        return info;

    }
    return null;
  }

  /**
   * 对Job排序 首先设置空闲时间 根据空闲时间对Job排序
   */
  public synchronized void reOrderJobs() {
    synchronized (jobQueue) {

      // 设置空闲时间
      for(JobSchedulingInfo info :jobQueue.keySet()){
        JobInProgress jip = info.jip;
        try {
          double AvgTaskTime = getAvgTaskTime(jip);
          if (0 < AvgTaskTime) {
            // 任务的平均时间
            int NumMaps = jip.desiredMaps() - jip.finishedMaps();
            int NumReduce = jip.desiredReduces()
                - jip.finishedReduces();// unFinishedd reduce
            // task数目
            int numMapSlot = NumMaps
                / LSSJobInProgressListener.NUMMapSlots;
            int numReduceSlot = NumReduce
                / LSSJobInProgressListener.NUMReduceSlots;

            if (numMapSlot * LSSJobInProgressListener.NUMMapSlots < NumMaps)
              numMapSlot++;

            if (numReduceSlot
                * LSSJobInProgressListener.NUMReduceSlots < NumReduce)
              numReduceSlot++;

            double remainingTime = (numReduceSlot + numMapSlot)
                * AvgTaskTime;// 任务的剩余时间

            // remaingTime-=正在运行的map task的执行时间/Map槽数
            if (0 < jip.runningMaps()) {
              TaskInProgress[] maps = jip.maps;
              double runningMapTime = 0;
              for (int i = 0; i < maps.length; i++) {
                if (maps[i].isRunning()
                    && System.currentTimeMillis()
                    - maps[i].getExecStartTime() > 0) {
                  runningMapTime += System
                      .currentTimeMillis()
                      - maps[i].getExecStartTime();
                }
              }
              remainingTime -= runningMapTime
                  / LSSJobInProgressListener.NUMMapSlots;
            }
            // remainingTime-=正在运行的reduce task的执行时间／reduce槽数
            if (0 < jip.runningReduces()) {
              // 加上正在运行的reduce task的执行时间
              TaskInProgress[] reduces = jip.reduces;
              double runningReduceTime = 0;
              for (int i = 0; i < reduces.length; i++) {
                if (reduces[i].isRunning()
                    && System.currentTimeMillis()
                    - reduces[i].getExecStartTime() > 0) {
                  runningReduceTime += System
                      .currentTimeMillis()
                      - reduces[i].getExecStartTime();
                }
              }
              remainingTime -= runningReduceTime
                  / LSSJobInProgressListener.NUMReduceSlots;
            }
            double spareTime = info.getDeadLine()
                - System.currentTimeMillis() - remainingTime;
            info.setSpareTime(spareTime);
            info.setAvgTime(AvgTaskTime);
            if (info.getSpareTime() + info.getAvgTime() <= 0) {
              String content = "" + info.jip.getJobID()
                  + "\tSpareTime:" + spareTime
                  + "\tAvgTaskTime:" + AvgTaskTime;
              content += "\nUnFinishedMaps:" + NumMaps
                  + "\tUnFinishedReduce:" + NumReduce;
              content += "\ndeadLine:"
                  + (info.getDeadLine() - System
                  .currentTimeMillis())
                  + "\tremainingTime:" + remainingTime;
              content += "\nExecTime:"
                  + (System.currentTimeMillis() - info.jip.startTime);
              writeFile("/SpareTime/" + info.jip.getJobID() + "."
                  + System.currentTimeMillis(), content);
            }
          } else {
            info.setSpareTime(info.getDeadLine()
                - System.currentTimeMillis());
          }
        } catch (Exception e) {

          info.setSpareTime(info.getDeadLine()
              - System.currentTimeMillis());
        }

      }

      // 插入到newQueue,排序
      Map<JobSchedulingInfo, JobInProgress> newQueue = new TreeMap<JobSchedulingInfo, JobInProgress>(
          LLF_JOB_QUEUE_COMPARATOR);
      for (JobSchedulingInfo info : jobQueue.keySet()) {
        newQueue.put(info, info.jip);
      }
      jobQueue = newQueue;
    }
  }

  /*
   * 删掉超时的，或者会超时的
   */
  public synchronized void killSpillDeadline() {
    synchronized (jobQueue) {
      for(JobSchedulingInfo info : jobQueue.keySet()) {
        if (info.getDeadLine() <= System.currentTimeMillis()) {
          info.jip.kill();
        }
        // 空闲时间+平均时间<=0,则kill
        else if (info.getSpareTime() + info.getAvgTime() <= 0) {
          info.jip.kill();
        }
      }
    }
  }

  /**
   * 存在返回正常值 不存在，返回 0
   */
  public synchronized double getAvgTaskTime(JobInProgress job) {
    double totalTime = 0, num = 0;
    Vector<TaskInProgress> finishedMaps = job.reportTasksInProgress(true,
        true);
    for (TaskInProgress task : finishedMaps) {
      totalTime += task.getExecFinishTime() - task.getExecStartTime();
    }

    Vector<TaskInProgress> finishedReduces = job.reportTasksInProgress(
        false, true);
    for (TaskInProgress task : finishedReduces) {
      totalTime += task.getExecFinishTime() - task.getExecStartTime();
    }

    num = finishedMaps.size() + finishedReduces.size();
    if (num > 0) {
      return totalTime / num;
    } else
      return 0;
  }

  /**
   * 写文件
   */
  public synchronized void writeFile(String path, String content) {
    try {
      Enumeration allNetInterfaces;
      try {
        allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
          NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
              .nextElement();
          // System.out.println(netInterface.getName());
          Enumeration addresses = netInterface.getInetAddresses();
          while (addresses.hasMoreElements()) {
            ip = (InetAddress) addresses.nextElement();
            if (ip != null && ip instanceof Inet4Address) {
              content += "\n本机的IP = " + ip.getHostAddress();
            }
          }
        }
      } catch (SocketException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      content += "\nCurrentTime:" + System.currentTimeMillis();
      FileSystem fs = FileSystem.get(new Configuration());
      FSDataOutputStream out = fs.create(new Path(path));
      out.writeUTF(content);
      out.close();

    } catch (Exception e) {
    }
  }
}