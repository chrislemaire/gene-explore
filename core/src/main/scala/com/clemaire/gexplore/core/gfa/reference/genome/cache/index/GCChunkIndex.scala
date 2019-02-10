package com.clemaire.gexplore.core.gfa.reference.genome.cache.index

import com.clemaire.cache.definitions.index.ChunkIndex
import com.lodborg.intervaltree.IntegerInterval

trait GCChunkIndex
  extends ChunkIndex {

  val relativeCoordinates: Map[Int, Long]
}

object GCChunkIndex {
  private case class GCChunkIndexImpl
  (id: Int,
   filePosition: Long,
   length: Long,
   ids: IntegerInterval,
   relativeCoordinates: Map[Int, Long]) extends GCChunkIndex

  def apply(id: Int,
            filePosition: Long,
            length: Long,
            ids: IntegerInterval,
            relativeCoordinates: Map[Int, Long]): GCChunkIndex =
    GCChunkIndexImpl(id, filePosition, length, ids, relativeCoordinates)
}
