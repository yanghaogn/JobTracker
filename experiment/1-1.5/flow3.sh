#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow3/out1
bin/hadoop dfs -rmr /yanghao/flow3/out2
bin/hadoop dfs -rmr /yanghao/flow3/out3
bin/hadoop dfs -rmr /yanghao/flow3/out4
bin/hadoop dfs -rmr /yanghao/flow3/out5
bin/hadoop dfs -rmr /yanghao/flow3/out6

sleep 199 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out1 -deadline 211 -period 211 -name flow3
sleep 216 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out2 -deadline 265 -period 265 -name flow3
sleep 197 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out3 -deadline 249 -period 249 -name flow3
sleep 195 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out4 -deadline 278 -period 278 -name flow3
sleep 194 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out5 -deadline 272 -period 272 -name flow3
sleep 206 
gnome-terminal -x  bin/hadoop jar WordCount.jar /yanghao/flow3/in /yanghao/flow3/out6 -deadline 290 -period 290 -name flow3


 
 
 
 
 
 
 




 

 
 

