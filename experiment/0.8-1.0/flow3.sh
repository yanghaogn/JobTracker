#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow3/out1
bin/hadoop dfs -rmr /yanghao/flow3/out2
bin/hadoop dfs -rmr /yanghao/flow3/out3
bin/hadoop dfs -rmr /yanghao/flow3/out4
bin/hadoop dfs -rmr /yanghao/flow3/out5
bin/hadoop dfs -rmr /yanghao/flow3/out6
sleep 199 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out1 -deadline 190 -period 190 -name flow3
sleep 216 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out2 -deadline 170 -period 170 -name flow3
sleep 197 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out3 -deadline 180 -period 180 -name flow3
sleep 195 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out4 -deadline 188 -period 188 -name flow3
sleep 194 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out5 -deadline 196 -period 196 -name flow3
sleep 206 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out6 -deadline 198 -period 198 -name flow3


 
 
 
 
 
 
 




 

 
 

