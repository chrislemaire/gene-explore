# Data Formats
## 1. Reference data
Reference data is the first abstraction layer in which the nodes and edges are interpreted and stored minimally.
The goal is to parse and copy over the original GFA information adding interpretation information wherever possible.
The format of the data reflects the minimalist approach to reference data. All node and edge information is stored relative to nodes.
Every single node is eventually stored as a reference node. Each node has the following format on disk:

1. 4 bytes for the node ID.
2. 8 bytes for the original file position where the node is stored.
3. 4 bytes for the length of the content (nucleotide string).
4. 4 bytes for the number of outgoing edges.
5. 4 bytes for the number of incoming edges.
6. A variable number of bytes for the incoming edges where each edge is 12 bytes.
7. A variable number of bytes for the outgoing edges where each edge is 12 bytes.

## 2. Reference index
The reference index is a data store that is small enough to always be read. The index data contains information about the different chunks that are in the cache.
Chunk information can only be retrieved by means of the index and not from the reference cache.
The format for each chunk stored in the index is given in the following description:

1. 4 bytes for the chunk ID.
2. 8 bytes for the position the first node in this chunk starts.
3. 4 bytes for the number of nodes in the chunk.
4. 4 bytes for the left-most layer in the chunk.
5. 4 bytes for the right-most layer in the chunk.
6. 4 bytes for the lowest segment ID.
7. 4 bytes for the highest segment ID.

## 3. Genome coordinates
Each node has some genome(s) running through that node as the variation has to be connected to at least one mutation.
Each of these genomes will follow their own paths through the graph and therefore should have an own coordinate system.
The coordinates are stored in a separate cache-index file-pair as the number of coordinates get problematically large in graphs like Tomato where 72 different genomes will have coordinates for each node.
The way the coordinates themselves are stored is described here:

1. 4 bytes for the number of genome coordinate pairs `k`.
2. 4 bytes for the node ID for which the genome coordinates will be given.
3. `k` genome coordinate pairs where each pair consists of 4 bytes to give the genome ID and 4 bytes to give the relative coordinates.

## 4. Genome coordinate index
As one might notice from the previous section, the coordinate pair stores relative coordinates. These are relative to the coordinates of their corresponding chunk described in the genome index file.
Each indexed chunk is formatted in the following way:

1. 4 bytes for the chunk ID.
2. 8 bytes for the position the chunk starts in the genome coordinates cache file.
3. 4 bytes for the number of bytes this chunk uses up.
4. 4 bytes for the left-most layer stored in the chunk.
5. 4 bytes for the right-most layer stored in the chunk.
6. 4 bytes for the lowest segment ID stored in the chunk.
7. 4 bytes for the highest segment ID stored in the chunk.
8. `k` genome coordinate pairs where each genome ID is paired with the current coordinate.

## 5. Heat-map
The format for this data is simple, it only includes a count of the number of nodes in each layer.
The file consists of pairs in the following format:

1. 4 bytes for the layer number.
2. 4 bytes for the number of nodes in that layer.
