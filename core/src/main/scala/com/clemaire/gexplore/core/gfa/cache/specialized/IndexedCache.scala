package com.clemaire.gexplore.core.gfa.cache.specialized

import com.clemaire.gexplore.core.gfa.cache.{Cache, Chunk}
import com.clemaire.gexplore.core.gfa.data.Identifiable
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait IndexedCache[C <: Chunk[CI, D], CI <: ChunkIndex, D <: Identifiable]
  extends Cache[C, CI] {

  def getById(segmentId: Int): Option[D] =
    load(index.betweenSegmentIds(segmentId, segmentId)).values
      .flatMap(_.data)
      .find(_._1 == segmentId)
      .map(_._2)

  def betweenIds(leftId: Int,
                 rightId: Int): Map[Int, D] =
    load(index.betweenSegmentIds(leftId, rightId)).values
      .flatMap(_.data)
      .filter(p => leftId <= p._1 && p._1 <= rightId)
      .toMap

}
