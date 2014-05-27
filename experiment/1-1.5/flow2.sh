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
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out1 -deadline 129 -period 129 -name flow2
sleep 108 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out2 -deadline 145 -period 145 -name flow2
sleep 151 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out3 -deadline 175 -period 175 -name flow2
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out4 -deadline 168 -period 168 -name flow2
sleep 123 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out5 -deadline 178 -period 178 -name flow2
sleep 146 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out6 -deadline 159 -period 159 -name flow2
sleep 124 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out7 -deadline 122 -period 122 -name flow2
sleep 114 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out8 -deadline 171 -period 171 -name flow2
sleep 135 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out9 -deadline 176 -period 176 -name flow2
sleep 112 
gnome-terminal -x  bin/hadoop jar TeraSort.jar /yanghao/flow2/in /yanghao/flow2/out10 -deadline 161 -period 161 -name flow2

 
  
 
 
 
 
 
 
 
 
 
 
 
 
 
















 
 
 
 
 
 
 
 









 
 
 
 
 
 
 

