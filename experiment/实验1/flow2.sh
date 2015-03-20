#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow2

sleep 212 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out1 
sleep 261 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out2 
sleep 281 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out3 
sleep 241 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out4 
sleep 270 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out5 
sleep 231 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out6 
sleep 260
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=260 -D user.period=260 -D flow=flow2 /wordcount/1030 /wordcount/flow2/out7 