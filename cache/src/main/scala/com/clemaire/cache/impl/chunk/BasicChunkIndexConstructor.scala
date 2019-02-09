package com.clemaire.cache.impl.chunk

import com.clemaire.cache.definitions.chunk.ChunkIndexConstructor
import com.clemaire.cache.definitions.index.ChunkIndex

trait BasicChunkIndexConstructor
  extends ChunkIndexConstructor[ChunkIndex] {

  /**
    * Constructs a new [[ChunkIndex]] of type [[ChunkIndex]]
    * from the current state.
    *
    * @return The built [[ChunkIndex]].
    */
  override protected def constructChunkIndex: ChunkIndex =
    ChunkIndex(chunkId, filePosition, dataLength, ids.toInterval)

}
