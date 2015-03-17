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
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=116 -D user.period=116 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out1 
sleep 108 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=118 -D user.period=118 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out2 
sleep 132 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=99 -D user.period=99 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out3 
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=118 -D user.period=118 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out4
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=111 -D user.period=111 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out5
sleep 106 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=98 -D user.period=98 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out6
sleep 119 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=103 -D user.period=103 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out7
sleep 123 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=109 -D user.period=109 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out8
sleep 110 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=119  -D user.period=119 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out9
sleep 103 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=119 -D user.period=119 -D flow=flow1 /yanghao/flow1/in /yanghao/flow1/out10
 



 
 
 
 
 
 
 
 









 

















