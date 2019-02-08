package com.clemaire.gexplore.core.gfa.reference.index

import com.lodborg.intervaltree.IntegerInterval

case class GCChunkIndex private
(id: Int,
 filePos: Long,
 length: Int,
 layers: IntegerInterval,
 segmentIds: IntegerInterval,
 referenceCoordinates: Map[Int, Long]) extends ChunkIndex

class GCIndex
  extends Index[GCChunkIndex]
