/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.mapred;

import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobStatusChangeEvent.EventType;

public class LSSJobInProgressListener extends JobInProgressListener {
  /**
   * 集群的状态
   */
  public static int NUMMapSlots ;// 集群的Map Slots数目
  public static int NUMReduceSlots ;// 集群的Reduce Slots数目
  public static double HeartbeatTime ;

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
    void setSpareTime(double time) {
      spareTime = time;
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
            this.priority == that.priority);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return (int) (id.hashCode() * priority.hashCode() + startTime);
    }
  }

  public static final Comparator<JobSchedulingInfo> LSS_JOB_QUEUE_COMPARATOR = new Comparator<JobSchedulingInfo>() {
    // 若o1的优先级高于o2，则返回负数
    public int compare(JobSchedulingInfo o1, JobSchedulingInfo o2) {


      // FIFO调度器
      int res = o1.getPriority().compareTo(o2.getPriority());
      if (res == 0) {
        /**
         * 空闲时间>0,空闲时间越短，优先级越高
         * 若存在job的空闲时间<0,空闲时间越长，优先级越高
         */
        double d1 = o1.getSpareTime();
        double d2 = o2.getSpareTime();
        if (d1 - d2 < 0) {
          res = -1;
        } else if (d1 - d2 > 0) {
          res = 1;
        }
      }
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
        LSS_JOB_QUEUE_COMPARATOR));
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

    jobQueue.put(new JobSchedulingInfo(job), job);

  }

  // Job will be removed once the job completes
  @Override
  public void jobRemoved(JobInProgress job) {
  }

  private void jobCompleted(JobSchedulingInfo oldInfo) {

    jobQueue.remove(oldInfo);

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


  double calRemainingTime(double avgTime, int totalSlots, int numWaiting, int numRunning, List<Long> listExecTime) {
    if (numWaiting + numRunning <= 0) {
      return 0;
    }
    double remainingTime = 0;
    remainingTime += numWaiting / totalSlots * (avgTime + 0.5 * HeartbeatTime);
    numWaiting -= numWaiting / totalSlots * totalSlots;
    if (numRunning + numWaiting <= totalSlots) {
      if (numWaiting == 0) {
        if (numRunning > 0) {
          remainingTime += avgTime - listExecTime.get(0);
        }
      } else {
        remainingTime += avgTime + 0.5 * HeartbeatTime;
      }
    } else {
      remainingTime += avgTime + 0.5 * HeartbeatTime + avgTime - listExecTime.get(totalSlots - numWaiting);
    }
    return remainingTime;
  }

  /**
   * 对Job排序 首先设置空闲时间 根据空闲时间对Job排序
   */
  public synchronized void reOrderJobs() {
    synchronized (jobQueue) {
      // 设置空闲时间
      for (JobSchedulingInfo jobInfo : jobQueue.keySet()) {
        JobInProgress jip = jobInfo.jip;

        if (jip.finishedMaps() + jip.finishedReduces() > 0) {
          int numRunningMap = 0;
          int numRunningReduce = 0;
          int numFinished = 0;
          int numWaitingMap = 0;
          int numWaitingReduce = 0;
          double totalFinishedTime = 0;
          List<Long> listExecTime = new LinkedList<Long>();
          for (TaskInProgress tip : jip.maps) {
            if (tip.isFailed()) {
              continue;
            }
            if (tip.isComplete()) {
              totalFinishedTime = tip.getExecFinishTime() - tip.getExecStartTime();
              numFinished++;
            } else {
              if (tip.isRunning()) {
                numRunningMap++;
                listExecTime.add(System.currentTimeMillis() - tip
                    .getExecStartTime());
              } else {
                numWaitingMap++;
              }
            }
          }
          for (TaskInProgress tip : jip.reduces) {
            if (tip.isFailed()) {
              continue;
            }
            if (tip.isComplete()) {
              totalFinishedTime = tip.getExecFinishTime() - tip.getExecStartTime();
              numFinished++;
            } else {
              if (tip.isRunning()) {
                numRunningReduce++;
                listExecTime.add(System.currentTimeMillis() - tip
                    .getExecStartTime());
              } else {
                numWaitingReduce++;
              }
            }
          }
          Collections.sort(listExecTime);
          double avgTime = totalFinishedTime / numFinished;
          double remainingTime = calRemainingTime(avgTime, NUMMapSlots, numWaitingMap, numRunningMap, listExecTime);
          remainingTime += calRemainingTime(avgTime, NUMReduceSlots, numWaitingReduce, numRunningReduce, listExecTime);
          double spareTime = jobInfo.getDeadLine()
              - System.currentTimeMillis() - remainingTime;
          jobInfo.setSpareTime(spareTime);
        } else {
          jobInfo.setSpareTime(jobInfo.getDeadLine()
              - System.currentTimeMillis());
        }
      }
      // 重新排序
      TreeMap<JobSchedulingInfo, JobInProgress> tempQueue = new TreeMap<JobSchedulingInfo, JobInProgress>(
          LSS_JOB_QUEUE_COMPARATOR);
      for (JobSchedulingInfo jobInfo : jobQueue.keySet()) {
        tempQueue.put(jobInfo, jobInfo.jip);
      }
      jobQueue.clear();
      jobQueue.putAll(tempQueue);
    }
  }

  /**
   * 删掉超时的，或者会超时的
   */
  public synchronized void killSpillDeadline() {
    synchronized (jobQueue) {
      for (JobSchedulingInfo info : jobQueue.keySet()) {
        if (info.getDeadLine() < System.currentTimeMillis()) {
          info.jip.kill();
        }
        // 空闲时间 + 0.5 * HeartbeatTime<=0,则kill
        else if (info.getSpareTime() + 0.5 * HeartbeatTime <= 0) {
          info.jip.kill();
        }
      }
    }
  }
}