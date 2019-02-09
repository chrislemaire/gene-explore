package com.clemaire.cache.impl.chunk

import com.clemaire.cache.definitions.chunk.{ChunkBuilder, ChunkIndexConstructor}
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.impl.chunk.capacity.SetBytes
import com.clemaire.interval.IntInterval

abstract class BasicChunkBuilder[D <: Identifiable, CI <: ChunkIndex](val max: Int = 8192)
  extends ChunkBuilder[D, CI]
    with ChunkIndexConstructor[CI]
    with SetBytes {

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
  override def register(data: D, length: Int): Option[CI] = {
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

}
