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
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out1 -deadline 169 -period 169 -name flow1
sleep 108 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out2 -deadline 174 -period 174 -name flow1
sleep 132 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out3 -deadline 175 -period 175 -name flow1
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out4 -deadline 158 -period 158 -name flow1
sleep 133 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out5 -deadline 126 -period 126 -name flow1
sleep 106 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out6 -deadline 137 -period 137 -name flow1
sleep 119 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out7 -deadline 153 -period 153 -name flow1
sleep 123 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out8 -deadline 177 -period 177 -name flow1
sleep 110 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out9 -deadline 178  -period 178 -name flow1
sleep 103 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow1/in /yanghao/flow1/out10 -deadline 129 -period 129 -name flow1
 
 
 
 
 
  
 
 
 
 
 



 
 
 
 
 
 
 
 









 

















