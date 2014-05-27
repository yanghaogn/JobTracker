#!/bin/bash
 
bin/hadoop dfs -rmr /yanghao/flow2/in/_partition.lst
bin/hadoop dfs -rmr /yanghao/flow2/out1
bin/hadoop dfs -rmr /yanghao/flow2/out2
bin/hadoop dfs -rmr /yanghao/flow2/out3
bin/hadoop dfs -rmr /yanghao/flow2/out4
bin/hadoop dfs -rmr /yanghao/flow2/out5
bin/hadoop dfs -rmr /yanghao/flow2/out6
bin/hadoop dfs -rmr /yanghao/flow2/out7
bin/hadoop dfs -rmr /yanghao/flow2/out8
bin/hadoop dfs -rmr /yanghao/flow2/out9
bin/hadoop dfs -rmr /yanghao/flow2/out10
sleep 118 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out1 -deadline 99 -period 99 -name flow2
sleep 108 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out2 -deadline 106 -period 106 -name flow2
sleep 151 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out3 -deadline 118 -period 118 -name flow2
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out4 -deadline 115 -period 115 -name flow2
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out5 -deadline 119 -period 119 -name flow2
sleep 146 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out6 -deadline 112 -period 112 -name flow2
sleep 124 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out7 -deadline 97 -period 97 -name flow2
sleep 114 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out8 -deadline 116 -period 116 -name flow2
sleep 135 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out9 -deadline 118 -period 118 -name flow2
sleep 112 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out10 -deadline 112 -period 112 -name flow2

 
 
 
 
 
 
 
 
 
 
















 
 
 
 
 
 
 
 









 
 
 
 
 
 
 

