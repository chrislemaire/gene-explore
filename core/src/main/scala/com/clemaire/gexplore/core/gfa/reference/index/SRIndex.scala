package com.clemaire.gexplore.core.gfa.reference.index

import com.lodborg.intervaltree.IntegerInterval

case class SRChunkIndex private[index]
(override val id: Int,
 override val filePos: Long,
 override val length: Int,
 override val layers: IntegerInterval,
 override val segmentIds: IntegerInterval) extends ChunkIndex

class SRIndex
  extends AbstractIndex[SRChunkIndex]
