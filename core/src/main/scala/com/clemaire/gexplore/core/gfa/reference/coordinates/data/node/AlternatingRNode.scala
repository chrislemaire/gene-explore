package com.clemaire.gexplore.core.gfa.reference.coordinates.data.node

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode

class AlternatingRNode(val id: Int,
                       val layer: Int,
                       _incoming: AlternatingNode,
                       _outgoing: AlternatingNode)
  extends AlternatingNode {

  override val incoming: Traversable[AlternatingNode] = List(_incoming)
  override val outgoing: Traversable[AlternatingNode] = List(_outgoing)

  override def isRNode: Boolean = true
}