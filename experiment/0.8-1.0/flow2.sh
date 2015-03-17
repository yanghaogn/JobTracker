#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow2/in/_partition.lst
bin/hadoop dfs -rmr /yanghao/flow2/out1
bin/hadoop dfs -rmr /yanghao/flow2/out2
bin/hadoop dfs -rmr /yanghao/flow2/out3
bin/hadoop dfs -rmr /yanghao/flow2/out4
bin/hadoop dfs -rmr /yanghao/flow2/out5
bin/hadoop dfs -rmr /yanghao/flow2/out6
bin/hadoop dfs -rmr /yanghao/flow2/out7
bin/hadoop dfs -rmr /yanghao/flow2/out8
bin/hadoop dfs -rmr /yanghao/flow2/out9
bin/hadoop dfs -rmr /yanghao/flow2/out10
sleep 118 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=99 -D user.period=99 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out1
sleep 108 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=106 -D user.period=106 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out2
sleep 151 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=118 -D user.period=118 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out3
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=115 -D user.period=115 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out4
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=119 -D user.period=119 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out5
sleep 146 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=112 -D user.period=112 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out6
sleep 124 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=97 -D user.period=97 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out7
sleep 114 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=116 -D user.period=116 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out8
sleep 135 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=118 -D user.period=118 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out9
sleep 112 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=112 -D user.period=112 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out10

 
 
 
 
 
 
 
 
 
 
















 
 
 
 
 
 
 
 









 
 
 
 
 
 
 

