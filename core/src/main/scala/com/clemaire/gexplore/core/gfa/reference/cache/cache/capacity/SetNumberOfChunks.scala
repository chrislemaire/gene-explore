package com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity

import com.clemaire.gexplore.core.gfa.reference.cache.Cache

trait SetNumberOfChunks
  extends Cache[_, _, _] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.size >= max
}
