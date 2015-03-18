package org.apache.hadoop.mapred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;


import org.apache.hadoop.mapred.FIFOJobInProgressListener.JobSchedulingInfo;
import org.apache.hadoop.mapreduce.server.jobtracker.TaskTracker;

import java.util.Collection;
import java.util.List;

import org.apache.hadoop.mapreduce.server.jobtracker.TaskTracker;

public class FIFOScheduler extends TaskScheduler {

  private static final int MIN_CLUSTER_SIZE_FOR_PADDING = 3;
  public static final Log LOG = LogFactory.getLog(JobQueueTaskScheduler.class);

  public static final Log LOGTASK = LogFactory.getLog("RUNNINGJOB");

  protected FIFOJobInProgressListener jobListener;
  protected EagerTaskInitializationListener eagerTaskInitializationListener;
  private float padFraction;

  public FIFOScheduler() {
    this.jobListener = new FIFOJobInProgressListener();
  }

  @Override
  public synchronized void start() throws IOException {
    super.start();
    taskTrackerManager.addJobInProgressListener(jobListener);
    eagerTaskInitializationListener.setTaskTrackerManager(taskTrackerManager);
    eagerTaskInitializationListener.start();
    taskTrackerManager.addJobInProgressListener(
        eagerTaskInitializationListener);

    new ResourceOccupyFIFO(jobListener).start();
  }

  @Override
  public synchronized void terminate() throws IOException {
    if (jobListener != null) {
      taskTrackerManager.removeJobInProgressListener(
          jobListener);
    }
    if (eagerTaskInitializationListener != null) {
      taskTrackerManager.removeJobInProgressListener(
          eagerTaskInitializationListener);
      eagerTaskInitializationListener.terminate();
    }
    super.terminate();
  }

  @Override
  public synchronized void setConf(Configuration conf) {
    super.setConf(conf);
    padFraction = conf.getFloat("mapred.jobtracker.taskalloc.capacitypad",
        0.01f);
    this.eagerTaskInitializationListener =
        new EagerTaskInitializationListener(conf);
  }

  @Override
  public synchronized List<Task> assignTasks(TaskTracker taskTracker)
      throws IOException {
    TaskTrackerStatus taskTrackerStatus = taskTracker.getStatus();
    ClusterStatus clusterStatus = taskTrackerManager.getClusterStatus();
    final int numTaskTrackers = clusterStatus.getTaskTrackers();
    final int clusterMapCapacity = clusterStatus.getMaxMapTasks();
    final int clusterReduceCapacity = clusterStatus.getMaxReduceTasks();

    Collection<JobInProgress> jobQueue =
        jobListener.getJobQueue();

    //
    // Get map + reduce counts for the current tracker.
    //
    final int trackerMapCapacity = taskTrackerStatus.getMaxMapSlots();
    final int trackerReduceCapacity = taskTrackerStatus.getMaxReduceSlots();
    final int trackerRunningMaps = taskTrackerStatus.countMapTasks();
    final int trackerRunningReduces = taskTrackerStatus.countReduceTasks();

    // Assigned tasks
    List<Task> assignedTasks = new ArrayList<Task>();

    //
    // Compute (running + pending) map and reduce task numbers across pool
    //
    int remainingReduceLoad = 0;
    int remainingMapLoad = 0;

    int numJOB = 0;


    synchronized (jobQueue) {
      for (JobInProgress job : jobQueue) {

        if (job.getStatus().getRunState() == JobStatus.RUNNING) {

          numJOB++;

          remainingMapLoad += (job.desiredMaps() - job.finishedMaps());

          if (remainingMapLoad >= 1) break;//大于1个，返回
          if (job.scheduleReduces()) {
            remainingReduceLoad +=
                (job.desiredReduces() - job.finishedReduces());
          }
        }
      }
    }


    LOGTASK.debug("DEBUGEDFSchedular****" + numJOB);
    LOGTASK.info("infoEDFSchedular****" + numJOB);

    // Compute the 'load factor' for maps and reduces
    double mapLoadFactor = 0.0;
    if (clusterMapCapacity > 0) {
      mapLoadFactor = (double) remainingMapLoad / clusterMapCapacity;
    }
    double reduceLoadFactor = 0.0;
    if (clusterReduceCapacity > 0) {
      reduceLoadFactor = (double) remainingReduceLoad / clusterReduceCapacity;
    }

    //
    // In the below steps, we allocate first map tasks (if appropriate),
    // and then reduce tasks if appropriate.  We go through all jobs
    // in order of job arrival; jobs only get serviced if their 
    // predecessors are serviced, too.
    //

    //
    // We assign tasks to the current taskTracker if the given machine 
    // has a workload that's less than the maximum load of that kind of
    // task.
    // However, if the cluster is close to getting loaded i.e. we don't
    // have enough _padding_ for speculative executions etc., we only 
    // schedule the "highest priority" task i.e. the task from the job 
    // with the highest priority.
    //

    final int trackerCurrentMapCapacity =
        Math.min(11, Math.min((int) Math.ceil(mapLoadFactor * trackerMapCapacity),
            trackerMapCapacity));

    int availableMapSlots = trackerCurrentMapCapacity - trackerRunningMaps;
    boolean exceededMapPadding = false;
    if (availableMapSlots > 0) {
      exceededMapPadding =
          exceededPadding(true, clusterStatus, trackerMapCapacity);
    }

    int numLocalMaps = 0;
    int numNonLocalMaps = 0;


    scheduleMaps:
    for (int i = 0; i < availableMapSlots; ++i) {
      synchronized (jobQueue) {
        for (JobInProgress job : jobQueue) {
          if (job.getStatus().getRunState() != JobStatus.RUNNING) {


            continue;
          }

          Task t = null;

          // Try to schedule a node-local or rack-local Map task
          t =
              job.obtainNewNodeOrRackLocalMapTask(taskTrackerStatus,
                  numTaskTrackers, taskTrackerManager.getNumberOfUniqueHosts());
          if (t != null) {
            assignedTasks.add(t);
            ++numLocalMaps;

            // Don't assign map tasks to the hilt!
            // Leave some free slots in the cluster for future task-failures,
            // speculative tasks etc. beyond the highest priority job
            if (exceededMapPadding) {
              break scheduleMaps;
            }

            // Try all jobs again for the next Map task 
            break;
          }

          // Try to schedule a node-local or rack-local Map task
          t =
              job.obtainNewNonLocalMapTask(taskTrackerStatus, numTaskTrackers,
                  taskTrackerManager.getNumberOfUniqueHosts());

          if (t != null) {
            assignedTasks.add(t);
            ++numNonLocalMaps;

            // We assign at most 1 off-switch or speculative task
            // This is to prevent TaskTrackers from stealing local-tasks
            // from other TaskTrackers.
            break scheduleMaps;
          }
        }
      }
    }
    int assignedMaps = assignedTasks.size();

    //
    // Same thing, but for reduce tasks
    // However we _never_ assign more than 1 reduce task per heartbeat
    //
    final int trackerCurrentReduceCapacity =
        Math.min(11, Math.min((int) Math.ceil(reduceLoadFactor * trackerReduceCapacity),
            trackerReduceCapacity));
    final int availableReduceSlots =
        Math.min((trackerCurrentReduceCapacity - trackerRunningReduces), 1);
    boolean exceededReducePadding = false;
    if (availableReduceSlots > 0) {
      exceededReducePadding = exceededPadding(false, clusterStatus,
          trackerReduceCapacity);
      synchronized (jobQueue) {
        for (JobInProgress job : jobQueue) {
          if (job.getStatus().getRunState() != JobStatus.RUNNING ||
              job.numReduceTasks == 0) {
            continue;
          }

          Task t =
              job.obtainNewReduceTask(taskTrackerStatus, numTaskTrackers,
                  taskTrackerManager.getNumberOfUniqueHosts()
              );
          if (t != null) {
            assignedTasks.add(t);
            break;
          }

          // Don't assign reduce tasks to the hilt!
          // Leave some free slots in the cluster for future task-failures,
          // speculative tasks etc. beyond the highest priority job
          if (exceededReducePadding) {
            break;
          }
        }
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Task assignments for " + taskTrackerStatus.getTrackerName() + " --> " +
          "[" + mapLoadFactor + ", " + trackerMapCapacity + ", " +
          trackerCurrentMapCapacity + ", " + trackerRunningMaps + "] -> [" +
          (trackerCurrentMapCapacity - trackerRunningMaps) + ", " +
          assignedMaps + " (" + numLocalMaps + ", " + numNonLocalMaps +
          ")] [" + reduceLoadFactor + ", " + trackerReduceCapacity + ", " +
          trackerCurrentReduceCapacity + "," + trackerRunningReduces +
          "] -> [" + (trackerCurrentReduceCapacity - trackerRunningReduces) +
          ", " + (assignedTasks.size() - assignedMaps) + "]");
    }


    return assignedTasks;
  }

  private boolean exceededPadding(boolean isMapTask,
                                  ClusterStatus clusterStatus,
                                  int maxTaskTrackerSlots) {
    int numTaskTrackers = clusterStatus.getTaskTrackers();
    int totalTasks =
        (isMapTask) ? clusterStatus.getMapTasks() :
            clusterStatus.getReduceTasks();
    int totalTaskCapacity =
        isMapTask ? clusterStatus.getMaxMapTasks() :
            clusterStatus.getMaxReduceTasks();

    Collection<JobInProgress> jobQueue =
        jobListener.getJobQueue();

    boolean exceededPadding = false;
    synchronized (jobQueue) {
      int totalNeededTasks = 0;
      for (JobInProgress job : jobQueue) {
        if (job.getStatus().getRunState() != JobStatus.RUNNING ||
            job.numReduceTasks == 0) {
          continue;
        }

        //
        // Beyond the highest-priority task, reserve a little 
        // room for failures and speculative executions; don't 
        // schedule tasks to the hilt.
        //
        totalNeededTasks +=
            isMapTask ? job.desiredMaps() : job.desiredReduces();
        int padding = 0;
        if (numTaskTrackers > MIN_CLUSTER_SIZE_FOR_PADDING) {
          padding =
              Math.min(maxTaskTrackerSlots,
                  (int) (totalNeededTasks * padFraction));
        }
        if (totalTasks + padding >= totalTaskCapacity) {
          exceededPadding = true;
          break;
        }
      }
    }

    return exceededPadding;
  }

  @Override
  public synchronized Collection<JobInProgress> getJobs(String queueName) {
    return jobListener.getJobQueue();
  }

}