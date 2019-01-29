package com.clemaire.gexplore.core.gfa.reference.index

import com.lodborg.intervaltree.IntegerInterval

case class NodeChunkIndex private[index]
(id: Int,
 filePos: Long,
 length: Int,
 layers: IntegerInterval,
 segmentIds: IntegerInterval) extends ChunkIndex

class NodeIndex
  extends AbstractIndex[NodeChunkIndex]
