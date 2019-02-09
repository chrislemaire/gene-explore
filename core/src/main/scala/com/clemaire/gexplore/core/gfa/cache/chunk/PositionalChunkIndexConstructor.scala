package com.clemaire.gexplore.core.gfa.cache.chunk

import com.clemaire.cache.definitions.chunk.ChunkIndexConstructor
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex

trait PositionalChunkIndexConstructor
  extends ChunkIndexConstructor[PositionalChunkIndex]
    with PositionalChunkBuilderData {

  /**
    * Constructs a new [[ChunkIndex]] of type [[PositionalChunkIndex]]
    * from the current state.
    *
    * @return The built [[ChunkIndex]].
    */
  protected[this] def constructChunkIndex: PositionalChunkIndex =
    PositionalChunkIndex(chunkId, filePosition, dataLength, ids.toInterval, layers.toInterval)

}
