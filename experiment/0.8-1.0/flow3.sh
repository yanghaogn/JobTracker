#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow3/out1
bin/hadoop dfs -rmr /yanghao/flow3/out2
bin/hadoop dfs -rmr /yanghao/flow3/out3
bin/hadoop dfs -rmr /yanghao/flow3/out4
bin/hadoop dfs -rmr /yanghao/flow3/out5
bin/hadoop dfs -rmr /yanghao/flow3/out6
sleep 199 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=190 -D user.period=190 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out1
sleep 216 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=170 -D user.period=170 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out2
sleep 197 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=180 -D user.period=180 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out3
sleep 195 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=188 -D user.period=188 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out4
sleep 194 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=196 -D user.period=196 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out5
sleep 206 
gnome-terminal -x  bin/hadoop jar WordCount.jar -D user.deadline=198 -D user.period=198 -D flow=flow3 /yanghao/flow3/in /yanghao/flow3/out6


 
 
 
 
 
 
 




 

 
 

