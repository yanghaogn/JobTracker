#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow3/out1
bin/hadoop dfs -rmr /yanghao/flow3/out2
bin/hadoop dfs -rmr /yanghao/flow3/out3
bin/hadoop dfs -rmr /yanghao/flow3/out4
bin/hadoop dfs -rmr /yanghao/flow3/out5
bin/hadoop dfs -rmr /yanghao/flow3/out6

sleep 199 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=211 -D user.period=211 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out1
sleep 216 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=265 -D user.period=265 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out2
sleep 197 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=249 -D user.period=249 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out3
sleep 195 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=278 -D user.period=278 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out4
sleep 194 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=272 -D user.period=272 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out5
sleep 206 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=290 -D user.period=290 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out6


 
 
 
 
 
 
 




 

 
 

