package com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity

import com.clemaire.gexplore.core.gfa.reference.cache.Cache
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait SetDataEntries[I <: AbstractIndex[CI], D, CI <: ChunkIndex]
  extends Cache[I, D, CI] {

  val max: Int

  override protected def reachedMax(): Boolean =
    loadedChunks.map(_._2.data.size).sum >= max

}
