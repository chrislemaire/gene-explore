package com.clemaire.gexplore.core.gfa.reference.coordinates

import com.clemaire.gexplore.core.gfa.reference.{AbstractIndex, ChunkIndex}
import com.lodborg.intervaltree.IntegerInterval

case class GenomeCoordinateChunkIndex private[coordinates]
(override val id: Int,
 override val filePos: Long,
 override val length: Int,
 override val layers: IntegerInterval,
 override val segmentIds: IntegerInterval,
 referenceCoordinates: Map[Int, Long]) extends ChunkIndex

class GenomeCoordinateIndex
  extends AbstractIndex[GenomeCoordinateChunkIndex]
