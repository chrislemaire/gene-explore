package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.{CapacityLimiter, Identifiable}
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.interval.IntInterval

trait ChunkBuilder[D <: Identifiable, CI <: ChunkIndex]
  extends Object
    with ChunkIndexConstructor[CI]
    with ChunkBuilderData
    with CapacityLimiter {

  /**
    * Resets the current chunk construction and increments
    * counters where necessary.
    */
  protected[this] def reset(): Unit = {
    chunkId += 1
    noEntries = 0
    filePosition += dataLength
    dataLength = 0

    ids = new IntInterval(Int.MaxValue, Int.MinValue)
  }

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
  def register(data: D, length: Int): Option[CI] = {
    ids.pushBoundaries(data.id)

    noEntries += 1
    dataLength += length

    if (full) {
      val result = Some(constructChunkIndex)
      reset()
      result
    } else {
      None
    }
  }

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
