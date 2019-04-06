package com.clemaire.gexplore.core.gfa.coordinates.data.node

import com.clemaire.gexplore.core.gfa.coordinates.data.{AlternatingNode, Segment}

class AlternatingQNode(val id: Int,
                       val layer: Int,
                       _segment: Segment,
                       _outgoing: AlternatingNode)
  extends AlternatingNode {

  override val incoming: Traversable[AlternatingNode] = List(_segment.p)
  override val outgoing: Traversable[AlternatingNode] = List(_outgoing)

  override def isQNode: Boolean = true
  override def segment: Segment = _segment
}