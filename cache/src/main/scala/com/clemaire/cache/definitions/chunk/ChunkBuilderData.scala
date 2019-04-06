package com.clemaire.cache.definitions.chunk

import com.clemaire.interval.IntInterval

trait ChunkBuilderData {

  /**
    * The index of the current chunk/ChunkIndex.
    */
  protected[this] var chunkId: Int = 0

  /**
    * The position the current chunk starts in
    * the corresponding data-file.
    */
  protected[this] var filePosition: Long = 0

  /**
    * The length of the current chunk in bytes.
    */
  protected[this] var dataLength: Long = 0

  /**
    * The number of data entries in the current
    * chunk.
    */
  protected[this] var noEntries: Int = 0

  /**
    * The ids currently covered by the chunk.
    */
  protected[this] var ids: IntInterval =
    new IntInterval(Int.MaxValue, Int.MinValue)

}
