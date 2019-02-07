package com.clemaire.gexplore.core.gfa.reference.coordinates.data.node

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.{Node, Segment}

class QNode(val id: Int,
            val layer: Int,
            _segment: Segment,
            _outgoing: Node)
  extends Node {

  override val incoming: Traversable[Node] = List(_segment.p)
  override val outgoing: Traversable[Node] = List(_outgoing)

  override def isQNode: Boolean = true
  override def segment: Segment = _segment
}