package com.clemaire.gexplore.core.gfa.coordinates.dummy.algorithm

import com.clemaire.gexplore.core.gfa.coordinates.dummy.{DummyNode, OrderNode, SegmentDummy}
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node.OrderNodeReadOnlyCache.OrderNodeReadOnlyCache

class DummyConstructor(nodes: OrderNodeReadOnlyCache,
                       private var id: Int) {

  def withOutgoing(node: OrderNode): (OrderNode, Map[Int, OrderNode]) =
    node -> nodes
      .get(node.outgoing)
      .filter(_._2.layer > node.layer + 1)

  def construct(layer: Traversable[OrderNode]): Traversable[DummyNode] = {
    layer.map(withOutgoing)
      .filter(_._2.nonEmpty)
      .flatMap(e => {
        e._2.map(out => {
          id += 1
          if (e._1.layer + 2 == out._2.layer) {
            new DummyNode(id, e._1.layer + 1, e._1.id, out._1)
          } else {
            new SegmentDummy(id, e._1.layer + 1, out._2.layer - 1, e._1.id, out._1)
          }
        })
      })
  }

}
