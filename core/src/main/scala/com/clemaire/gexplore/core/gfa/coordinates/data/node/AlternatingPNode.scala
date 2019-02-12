package com.clemaire.gexplore.core.gfa.coordinates.data.node

import com.clemaire.gexplore.core.gfa.coordinates.data.{AlternatingNode, Segment}

class AlternatingPNode(val id: Int,
                       val layer: Int,
                       _incoming: AlternatingNode,
                       _segment: Segment)
  extends AlternatingNode {

  override val incoming: Traversable[AlternatingNode] = List(_incoming)
  override val outgoing: Traversable[AlternatingNode] = List(_segment.q)

  override def isPNode: Boolean = true
  override def segment: Segment = _segment
}