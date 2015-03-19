#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow2

sleep 383 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out1 &
sleep 390 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out2 &
sleep 400 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out3 &
sleep 410 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out4 &
sleep 402 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out5 &
sleep 400 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=400 -D user.period=400 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out6 &
