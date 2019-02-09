package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.{CapacityLimiter, Identifiable}
import com.clemaire.cache.definitions.index.ChunkIndex

trait ChunkBuilder[D <: Identifiable, CI <: ChunkIndex]
  extends Object
    with ChunkIndexConstructor[CI]
    with ChunkBuilderData
    with CapacityLimiter {

  /**
    * Registers a data entry to the chunk. This
    * essentially adds the data id to the range of
    * the chunk and creates a [[ChunkIndex]] of the
    * chunk if necessary.
    *
    * @param data   The data to add to the chunk.
    * @param length The length of the data on disk.
    * @return The resulting [[ChunkIndex]] or none if
    *         none was created.
    */
  def register(data: D, length: Int): Option[CI]

  /**
    * Finishes the last remaining chunk and returns
    * its index if the chunk is non-empty.
    *
    * @return Some [[ChunkIndex]] if the current
    *         [[Chunk]] was non-empty.
    */
  def finish: Option[CI] =
    if (dataLength > 0)
      Some(constructChunkIndex)
    else
      None

}
