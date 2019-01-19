package com.clemaire.gexplore.core.gfa.reference.index

import com.lodborg.intervaltree.IntegerInterval

case class GCChunkIndex private
(override val id: Int,
 override val filePos: Long,
 override val length: Int,
 override val layers: IntegerInterval,
 override val segmentIds: IntegerInterval,
 referenceCoordinates: Map[Int, Long]) extends ChunkIndex

class GCIndex
  extends AbstractIndex[GCChunkIndex]
