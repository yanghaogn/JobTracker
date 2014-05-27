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
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out1 -deadline 116 -period 116 -name flow1
sleep 108 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out2 -deadline 118 -period 118 -name flow1
sleep 132 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out3 -deadline 99 -period 99 -name flow1
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out4 -deadline 118 -period 118 -name flow1
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out5 -deadline 111 -period 111 -name flow1
sleep 106 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out6 -deadline 98 -period 98 -name flow1
sleep 119 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out7 -deadline 103 -period 103 -name flow1
sleep 123 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out8 -deadline 109 -period 109 -name flow1
sleep 110 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out9 -deadline 119  -period 119 -name flow1
sleep 103 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out10 -deadline 119 -period 119 -name flow1
 



 
 
 
 
 
 
 
 









 

















