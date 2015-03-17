#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow4/in/_partition.lst
bin/hadoop dfs -rmr /yanghao/flow4/out1
bin/hadoop dfs -rmr /yanghao/flow4/out2
sleep 601 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=546 -D user.period=546 -D flow=flow4 /yanghao/flow4/in /yanghao/flow4/out1
sleep 582 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=516 -D user.period=516 -D flow=flow4 /yanghao/flow4/in /yanghao/flow4/out2



 

 
 
