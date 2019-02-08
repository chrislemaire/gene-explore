package com.clemaire.gexplore.core.gfa.reference.coordinates.assignment

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.SparseCompactionLayer
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode

class VerticalAlignment {

  def align(layer: SparseCompactionLayer,
            root: (AlternatingNode, AlternatingNode) => Unit,
            align: (AlternatingNode, AlternatingNode) => Unit): Unit = {
    val r = -1
    layer.order.filter(_.isLeft).foreach(e => {
      val node = e.left.get
      if (node.incoming.nonEmpty) {
        val sorted = node.incoming.toIndexedSeq.sortBy(_.position)
        val d = node.incoming.size
        (Math.floorDiv(d + 1, 2) to Math.ceil((d + 1) / 2.0).toInt).foreach(m => {
          if (r < sorted(m).position) {
            align(sorted(m), node)
            root(node, sorted(m))
            align(node, root)
          }
        })
      }
    })
  }

}
