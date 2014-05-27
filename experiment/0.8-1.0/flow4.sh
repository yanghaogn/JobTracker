#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow4/in/_partition.lst
bin/hadoop dfs -rmr /yanghao/flow4/out1
bin/hadoop dfs -rmr /yanghao/flow4/out2
sleep 601 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow4/in /yanghao/flow4/out1 -deadline 546 -period 546 -name flow4
sleep 582 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow4/in /yanghao/flow4/out2 -deadline 516 -period 516 -name flow4



 

 
 
