package com.clemaire.gexplore.core.gfa.cache.capacity

import com.clemaire.gexplore.core.gfa.cache.{CacheData, Chunk}

trait SetDataEntries[C <: Chunk[_, _]]
  extends CapacityLimiter
    with CacheData[C] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.map(_._2.data.size).sum >= max

}
