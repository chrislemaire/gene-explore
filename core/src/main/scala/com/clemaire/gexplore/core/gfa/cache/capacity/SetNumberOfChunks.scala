package com.clemaire.gexplore.core.gfa.cache.capacity

import com.clemaire.gexplore.core.gfa.cache.{CacheData, Chunk}
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait SetNumberOfChunks[CI <: ChunkIndex, D]
  extends CapacityLimiter
    with CacheData[Chunk[CI, D]] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.size >= max

}
