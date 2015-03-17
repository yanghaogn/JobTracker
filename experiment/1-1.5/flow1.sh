#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow1/out1
bin/hadoop dfs -rmr /yanghao/flow1/out2
bin/hadoop dfs -rmr /yanghao/flow1/out3
bin/hadoop dfs -rmr /yanghao/flow1/out4
bin/hadoop dfs -rmr /yanghao/flow1/out5
bin/hadoop dfs -rmr /yanghao/flow1/out6
bin/hadoop dfs -rmr /yanghao/flow1/out7
bin/hadoop dfs -rmr /yanghao/flow1/out8
bin/hadoop dfs -rmr /yanghao/flow1/out9
bin/hadoop dfs -rmr /yanghao/flow1/out10

sleep 122 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=169 -D user.period=169 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out1
sleep 108 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=174 -D user.period=174 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out2
sleep 132 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=175 -D user.period=175 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out3
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=158 -D user.period=158 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out4
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=126 -D user.period=126 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out5
sleep 106 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=137 -D user.period=137 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out6
sleep 119 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=153 -D user.period=153 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out7
sleep 123 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=177 -D user.period=177 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out8
sleep 110 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=178  -D user.period=178 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out9
sleep 103 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=129 -D user.period=129 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out10
 
 
 
 
 
  
 
 
 
 
 



 
 
 
 
 
 
 
 









 

















