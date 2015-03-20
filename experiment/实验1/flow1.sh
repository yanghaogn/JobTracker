#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow1
sleep 410 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=450 -D user.period=450 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out1 
sleep 420
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=450 -D user.period=450 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out2 
sleep 460
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=450 -D user.period=450 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out3 

