package com.clemaire.cache.definitions.io.reading

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.chunk.Chunk

trait ChunkReader[D <: Identifiable, CI <: ChunkIndex]
  extends AutoCloseable
    with DataReader[D] {

  /**
    * Constructs a [[Chunk]] from its id and data.
    *
    * @param id   The unique identifier for the [[Chunk]].
    * @param data The data passed to the [[Chunk]].
    * @return The constructed [[Chunk]].
    */
  def constructChunk(id: Int, data: Map[Int, D]): Chunk[D]

  /**
    * Reads a single chunk from the input source starting
    * at the file position and ending at the file position +
    * length in the given [[ChunkIndex]].
    *
    * @param index The [[ChunkIndex]] to read the chunk of.
    * @return The read [[Chunk]].
    */
  def read(index: CI): Chunk[D]

  /**
    * Reads a list of chunks from the input source. Each chunk
    * is read by the file position and length indicated in its
    * respective [[ChunkIndex]].
    *
    * @param indices The [[ChunkIndex]]es to read chunks of.
    * @return A [[Traversable]] containing the read [[Chunk]]s.
    */
  def read(indices: Traversable[CI]): Traversable[Chunk[D]] =
    indices.map(read)

}
