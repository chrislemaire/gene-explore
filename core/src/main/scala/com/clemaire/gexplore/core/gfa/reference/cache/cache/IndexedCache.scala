package com.clemaire.gexplore.core.gfa.reference.cache.cache

import com.clemaire.gexplore.core.gfa.reference.cache.Cache
import com.clemaire.gexplore.core.gfa.reference.data.Indexed
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait IndexedCache[I <: AbstractIndex[CI], D <: Indexed, CI <: ChunkIndex]
  extends Cache[I, D, CI] {

  def betweenIds(leftId: Int,
                 rightId: Int): Map[Int, D] =
    load(index.betweenSegmentIds(leftId, rightId)).values
      .flatMap(_.data)
      .filter(p => leftId <= p._2.id && p._2.id <= rightId)
      .toMap

}