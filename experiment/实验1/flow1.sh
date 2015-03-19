#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow1

sleep 480 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=500 -D user.period=500 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out1 &
sleep 490
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=500 -D user.period=500 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out2 &
sleep 510
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=500 -D user.period=500 -D flow=flow2 /wordcount/3100 /wordcount/flow1/out3 &

