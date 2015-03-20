#!/bin/bash
 
bin/hadoop dfs -rmr /wordcount/flow3

sleep 203 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out1 
sleep 171 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out2 
sleep 203 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out3 
sleep 218 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out4 
sleep 190 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out5 
sleep 200   
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out6 
sleep 197 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out7 
sleep 189 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out8 
sleep 164; 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out9 
sleep 191; 
gnome-terminal -x  bin/hadoop jar hadoop-examples-1.2.1.jar wordcount -D user.deadline=200 -D user.period=200 -D flow=flow3 /wordcount/640 /wordcount/flow3/out10
 
