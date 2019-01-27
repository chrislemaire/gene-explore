package com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity

import com.clemaire.gexplore.core.gfa.reference.cache.{CacheData, Chunk}

trait SetNumberOfChunks
  extends CapacityLimiter
    with CacheData[Chunk[_, _]] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.size >= max

}
