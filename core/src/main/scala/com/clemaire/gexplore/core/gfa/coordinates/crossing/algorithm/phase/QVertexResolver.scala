package com.clemaire.gexplore.core.gfa.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.coordinates.crossing.algorithm.ALData

trait QVertexResolver
  extends Object
    with ALData {

  def resolveQVertices(): Unit =
    qVertices.foreach(v => {
      val container = v.segment.node.get.getContainer
      val rightSplit = container.split(v.segment)

      val oldNext = container.next
      container
        .append(v)
        .append(rightSplit)
        .append(oldNext)
    })

}
