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
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=129 -D user.period=129 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out1
sleep 108 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=145 -D user.period=145 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out2
sleep 151 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=175 -D user.period=175 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out3
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=168 -D user.period=168 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out4
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=178 -D user.period=178 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out5
sleep 146 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=159 -D user.period=159 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out6
sleep 124 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=122 -D user.period=122 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out7
sleep 114 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=171 -D user.period=171 -D flow=flow2 /yanghao/flow2/in /yanghao/flow2/out8
sleep 135 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out9 -D user.deadline=176 -D user.period=176 -D flow=flow2
sleep 112 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out10 -D user.deadline=161 -D user.period=161 -D flow=flow2

 
  
 
 
 
 
 
 
 
 
 
 
 
 
 
















 
 
 
 
 
 
 
 









 
 
 
 
 
 
 

