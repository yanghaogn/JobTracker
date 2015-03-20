800M
```
bin/hadoop jar hadoop-examples-1.2.1.jar randomtextwriter  -D test.randomtextwrite.total_bytes=816578560 /wordcount/800
```
1.89G
```
bin/hadoop jar hadoop-examples-1.2.1.jar randomtextwriter  -D test.randomtextwrite.total_bytes=1974634526 -D test.randomtextwrite.bytes_per_map=1974634526 /wordcount/1890
```

4.78G
```
bin/hadoop jar hadoop-examples-1.2.1.jar randomtextwriter  -D test.randomtextwrite.total_bytes=5234491391 -D test.randomtextwrite.bytes_per_map=5234491391 /wordcount/4780
```