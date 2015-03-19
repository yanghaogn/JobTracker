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


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobStatusChangeEvent.EventType;

/**
 * A {@link JobInProgressListener} that maintains the jobs being managed in
 * a queue. By default the queue is FIFO, but it is possible to use custom
 * queue ordering by using the
 * {@link #RMSJobInProgressListener(Collection)} constructor.
 */
public class RMSJobInProgressListener extends JobInProgressListener {

  /** A class that groups all the information from a {@link JobInProgress} that 
   * is necessary for scheduling a job.
   */
  static class JobSchedulingInfo {
    private JobPriority priority;
    private long startTime;
    private JobID id;
    private long deadLine;//截止时间
    private long period;//周期
    JobInProgress jip;

    public JobSchedulingInfo(JobInProgress jip) {


      this(jip.getStatus(), jip);

    }

    public JobSchedulingInfo(JobStatus status, JobInProgress jip) {

      Configuration conf = jip.getJobConf();
      this.jip = jip;
      priority = status.getJobPriority();
      startTime = status.getStartTime();
      id = status.getJobID();
      //获取周期
      if (conf.getLong("user.period", Long.MAX_VALUE) == Long.MAX_VALUE) {
        period = Long.MAX_VALUE;
      } else {
        period = conf.getLong("user.period", 0) * 1000;
      }
      //获取截止时间
      if (conf.getLong("user.deadline", Long.MAX_VALUE) == Long.MAX_VALUE) {
        deadLine = Long.MAX_VALUE;
      } else {
        deadLine = startTime + conf.getLong("user.deadline", 0) * 1000;
      }


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

    long getPeriod() {
      return period;
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

  static final Comparator<JobSchedulingInfo> RMS_JOB_QUEUE_COMPARATOR
      = new Comparator<JobSchedulingInfo>() {
    //o1的优先级若高于o2，则返回负数
    public int compare(JobSchedulingInfo o1, JobSchedulingInfo o2) {

      int res = o1.getPriority().compareTo(o2.getPriority());
      if (res == 0) {
        /**
         * 周期越短，优先级越高 
         */
        if (o1.getPeriod() - o2.getPeriod() < 0) {
          res = -1;
        } else if (o1.getPeriod() - o2.getPeriod() > 0) {
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

  public RMSJobInProgressListener() {
    this(new TreeMap<JobSchedulingInfo,
        JobInProgress>(RMS_JOB_QUEUE_COMPARATOR));
  }

  /**
   * For clients that want to provide their own job priorities.
   *
   * @param jobQueue A collection whose iterator returns jobs in priority order.
   */
  protected RMSJobInProgressListener(Map<JobSchedulingInfo,
      JobInProgress> jobQueue) {
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
}