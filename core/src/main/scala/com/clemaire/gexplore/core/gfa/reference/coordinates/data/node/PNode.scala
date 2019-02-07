package com.clemaire.gexplore.core.gfa.reference.coordinates.data.node

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.{Node, Segment}

class PNode(val id: Int,
            val layer: Int,
            _incoming: Node,
            _segment: Segment)
  extends Node {

  override val incoming: Traversable[Node] = List(_incoming)
  override val outgoing: Traversable[Node] = List(_segment.q)

  override def isPNode: Boolean = true
  override def segment: Segment = _segment
}