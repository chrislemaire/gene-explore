package com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity

import com.clemaire.gexplore.core.gfa.reference.cache.Cache

trait SetDataEntries
  extends Cache[_, _, _] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.map(_._2.data.size).sum >= max

}
