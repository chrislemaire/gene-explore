package com.clemaire.gexplore.core.gfa.cache.specialized

import com.clemaire.gexplore.core.gfa.cache.{Cache, Chunk}
import com.clemaire.gexplore.core.gfa.data.Positional
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait PositionalCache[C <: Chunk[CI, D], CI <: ChunkIndex, D <: Positional]
  extends Cache[C, CI] {

  def getByLayer(layer: Int): Map[Int, D] =
    load(index.betweenLayers(layer, layer)).values
      .flatMap(_.data)
      .filter(_._2.layer == layer)
      .toMap

  def betweenLayers(left: Int,
                    right: Int): Map[Int, D] =
    load(index.betweenLayers(left, right)).values
      .flatMap(_.data)
      .filter(p => left <= p._2.layer && p._2.layer <= right)
      .toMap

}
