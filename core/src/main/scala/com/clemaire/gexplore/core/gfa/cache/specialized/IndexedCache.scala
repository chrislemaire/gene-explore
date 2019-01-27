package com.clemaire.gexplore.core.gfa.cache.specialized

import com.clemaire.gexplore.core.gfa.cache.Cache
import com.clemaire.gexplore.core.gfa.data.Indexed
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait IndexedCache[I <: AbstractIndex[CI], CI <: ChunkIndex, D <: Indexed]
  extends Cache[I, CI, D] {

    def betweenIds(leftId: Int,
                   rightId: Int): Map[Int, D] =
      load(index.betweenSegmentIds(leftId, rightId)).values
        .flatMap(_.data)
        .filter(p => leftId <= p._2.id && p._2.id <= rightId)
        .toMap

  }
