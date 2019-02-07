package com.clemaire.gexplore.core.gfa.reference.coordinates.data.node

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Node

class RNode(val id: Int,
            val layer: Int,
            _incoming: Node,
            _outgoing: Node)
  extends Node {

  override val incoming: Traversable[Node] = List(_incoming)
  override val outgoing: Traversable[Node] = List(_outgoing)

  override def isRNode: Boolean = true
}