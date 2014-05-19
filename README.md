<element style="margin:0em 0px 12px; padding:0px; font-family:Microsoft YaHei; font-size:22px; color:rgb(32,136,178); line-height:32px">JobTracker</element>
========

The default scheduler of Hadoop1 cannot deal with real-time jobs, so we add the real-time property to the job conf and change the scheduling stategy to ensure the real-time jobs.Several real-time strategies are applied and each are a scheduler here.  
The strategy is realized as follows:  
1. add the real-time property to the job
2. change the JobInProgressListener, and change the job queue in this file
3. change the method assignTasks() in Scheduler.java, add a module that kills the job which miss the deadlie
4. make the jar and change the configuration of Hadoop1
5. restart the Hadoop1 cluster



# <element style="margin:0em 0px 12px; padding:0px; font-family:Microsoft YaHei; font-size:22px; color:rgb(32,136,178); line-height:32px">FIFOScheduler</element>
The default scheduler schedules the jobs coming earlier
# <element style="margin:0em 0px 12px; padding:0px; font-family:Microsoft YaHei; font-size:22px; color:rgb(32,136,178); line-height:32px">EDFScheduler</element>
The job with earlier deadline will be scheduled first.
# <element style="margin:0em 0px 12px; padding:0px; font-family:Microsoft YaHei; font-size:22px; color:rgb(32,136,178); line-height:32px">LLFScheduler</element>
The job will be scheduled first with a less spare-time.The spare-time is the remaining computing time minus from deadline.So the remaing computing time should be estimated first.
# <element style="margin:0em 0px 12px; padding:0px; font-family:Microsoft YaHei; font-size:22px; color:rgb(32,136,178); line-height:32px">RMScheduler</element>
The job will be scheduled first in the workflow with a shorter period.