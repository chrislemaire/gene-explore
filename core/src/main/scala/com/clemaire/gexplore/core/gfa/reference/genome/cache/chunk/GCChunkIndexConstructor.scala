package com.clemaire.gexplore.core.gfa.reference.genome.cache.chunk

import com.clemaire.cache.definitions.chunk.ChunkIndexConstructor
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex

trait GCChunkIndexConstructor
  extends ChunkIndexConstructor[GCChunkIndex]
    with GCChunkBuilderData {

  /**
    * Constructs a new [[com.clemaire.cache.definitions.index.ChunkIndex]]
    * of type [[GCChunkIndex]] from the current state.
    *
    * @return The built [[com.clemaire.cache.definitions.index.ChunkIndex]].
    */
  override protected def constructChunkIndex: GCChunkIndex =
    GCChunkIndex(chunkId, filePosition, dataLength, ids.toInterval, relativeCoordinates.toMap)

}
