package org.apache.hadoop.mapred;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class ResourceOccupyRMS extends Thread {

  protected RMSJobInProgressListener jobListener;

  int[][] flow1;
  int[][] flow2;
  int[][] flow3;
  int[][] flow4;
  long[] time;

  ResourceOccupyRMS(RMSJobInProgressListener listen) {
    flow1 = new int[1850][3];
    flow2 = new int[1850][3];
    flow3 = new int[1850][3];
    flow4 = new int[1850][3];

    time = new long[1850];
    for (int i = 0; i < 1850; i++) {
      flow1[i][0] = 0;
      flow1[i][1] = 0;
      flow1[i][2] = 0;

      flow2[i][0] = 0;
      flow2[i][1] = 0;
      flow2[i][2] = 0;

      flow3[i][0] = 0;
      flow3[i][1] = 0;
      flow3[i][2] = 0;

      flow4[i][0] = 0;
      flow4[i][1] = 0;
      flow4[i][2] = 0;
    }
    jobListener = listen;
  }

  public void run() {

    Collection<JobInProgress> jobQueue;
    for (int i = 0; i < 1850; i++) {
      jobQueue = jobListener.getJobQueue();
      synchronized (jobQueue) {
        for (JobInProgress job : jobQueue) {

          try {
            if (job.getJobConf().get("flow").contains("flow1")) {
              flow1[i][0] += job.runningMaps();
              flow1[i][1] += job.runningReduces();
            }
            if (job.getJobConf().get("flow").contains("flow2")) {

              flow2[i][0] += job.runningMaps();
              flow2[i][1] += job.runningReduces();
            }
            if (job.getJobConf().get("flow").contains("flow3")) {
              flow3[i][0] += job.runningMaps();
              flow3[i][1] += job.runningReduces();
            }
            if (job.getJobConf().get("flow").contains("flow4")) {
              flow4[i][0] += job.runningMaps();
              flow4[i][1] += job.runningReduces();
            }
          } catch (Exception e) {
          }

        }
      }

      flow1[i][2] = flow1[i][0] + flow1[i][1];
      flow2[i][2] = flow2[i][0] + flow2[i][1];
      flow3[i][2] = flow3[i][0] + flow3[i][1];
      flow4[i][2] = flow4[i][0] + flow4[i][1];
      time[i] = System.currentTimeMillis();

      // 1秒钟采集一次
      try {
        sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    try {
      writeFile();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  void writeFile() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter(
        "/usr/local/ResourceSplit"));
    for (int i = 0; i < 1850; i++) {
      w.write(time[i] + "\t" + flow1[i][0] + "\t" + flow1[i][1] + "\t"
          + flow1[i][2] + "\t" + flow2[i][0] + "\t" + flow2[i][1]
          + "\t" + flow2[i][2] + "\t" + flow3[i][0] + "\t"
          + flow3[i][1] + "\t" + flow3[i][2] + "\t" + flow4[i][0]
          + "\t" + flow4[i][1] + "\t" + flow4[i][2] + "\n");
    }
    w.close();
  }
}
