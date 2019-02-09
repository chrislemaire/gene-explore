package com.clemaire.cache.impl.chunk

import com.clemaire.cache.definitions.chunk.ChunkIndexConstructor
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.impl.index.BasicChunkIndex

trait BasicChunkIndexConstructor
  extends ChunkIndexConstructor[ChunkIndex] {

  /**
    * Constructs a new [[ChunkIndex]] of type [[BasicChunkIndex]]
    * from the current state.
    *
    * @return The built [[ChunkIndex]].
    */
  override protected def constructChunkIndex: ChunkIndex =
    BasicChunkIndex(chunkId, filePosition, dataLength, ids.toInterval)

}
