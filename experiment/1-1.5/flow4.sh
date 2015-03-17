#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow4/in/_partition.lst
bin/hadoop dfs -rmr /yanghao/flow4/out1
bin/hadoop dfs -rmr /yanghao/flow4/out2

sleep 601 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=618 -D user.period=618 -D flow=flow4 /yanghao/flow4/in /yanghao/flow4/out1
sleep 582 
gnome-terminal -x  bin/hadoop jar TeraSort.jar -D user.deadline=805 -D user.period=805 -D flow=flow4 /yanghao/flow4/in /yanghao/flow4/out2



 

 
 
 
 
