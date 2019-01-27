package com.clemaire.gexplore.core.gfa.reference.cache.cache

import com.clemaire.gexplore.core.gfa.reference.cache.Cache
import com.clemaire.gexplore.core.gfa.reference.data.Positional
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait PositionalCache[I <: AbstractIndex[CI], D <: Positional, CI <: ChunkIndex]
  extends Cache[I, D, CI] {

  def betweenLayers(left: Int,
                    right: Int): Map[Int, D] =
    load(index.betweenLayers(left, right)).values
      .flatMap(_.data)
      .filter(p => left <= p._2.layer && p._2.layer <= right)
      .toMap

}
