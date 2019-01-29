# Readers
## AsyncNioBufferedLinearReader
* Mid-performance reader using Java NIO buffers to read without lookup.
* Buffers get loaded asynchronously as soon as the last buffer is fully read, buffers are swapped and the old buffer is used to read a new chunk of data.
* Not useful for reading chunks of data as no lookup is provided.
* Made from scratch.

## NioBufferedReader
* Mid-performance reader utilizing JavaNIO buffers.

# Writers
## AsyncNioBufferedWriter
* Mid-performance writer using Java NIO buffers.
* While buffers get written to file, other work can be done by means of the `acceptWork` function.
* Extends upon NioBufferedWriter by adding asynchronous reading.

## IoBufferedWriter
* High-performance writer using the Java IO DataOutputStream writer to provide a means of writing back data to disk.
* No asynchronous activities can happen. When a buffer gets written out to disk by DataOutputStream, all operations are blocked.