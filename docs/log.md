# Logbook of activities involving developing and testing of gene-explore
## 14-01-2019
* Figured out issue with reader comparison. Problem: side-by-side comparisons of byte-reads turned up differently. Solution: both readers were not reset after each full read. After reading all bytes, both readers only output 0 bytes.

## 15-01-2019
Start of the log.
* Updated Intellij to version 2018.3.3 from 2018.1.1
* Committed reader comparison files.
* 16:04 - As performed with repo as of 15-01-2019 16:04, the results of the test are as follows:

```
Testing started at 16:04 ...
Warmup:
NioBufferedReader: 555
BufferedReader: 272

Lines: run 1:
NioBufferedReader: 409
BufferedReader: 164

Lines: run 2:
NioBufferedReader: 380
BufferedReader: 144

Lines: run 3:
NioBufferedReader: 299
BufferedReader: 131

Bytes: run 1:
NioBufferedReader: 132
BufferedReader: 131

Bytes: run 2:
NioBufferedReader: 73
BufferedReader: 87

Bytes: run 3:
NioBufferedReader: 58
BufferedReader: 72


Process finished with exit code 0
```

* 16:34 - Started looking at low speeds of line reading in NioBufferedReader.
* 16:38 - Decided against inspecting low line read speeds, as reader will be used for byte reads only. Will instead work on getting reading functional.

## 16-01-2019
* 11:30 - Finished data reader for genome indices. Decided to work on index reading first as it is required first and not very complicated.
* Decided chunk length cannot be more than 2GB and the length thereof should therefore be stored as an integer instead of a double.
* Problem: GenomeIndexDataReader should know the number of genomes. Fix: required 'with' in GenomeIndexDataReader.
* 
