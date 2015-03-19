#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow3

sleep 198 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out1 &
sleep 201 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out2 &
sleep 220 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out3 &
sleep 198 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out4 &
sleep 190 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out5 &
sleep 220 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out6 &
sleep 221 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out7 &
sleep 201 
nohup  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out8 &