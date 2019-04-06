package com.clemaire.gexplore.core.gfa.coordinates.dummy.algorithm

import com.clemaire.gexplore.core.gfa.coordinates.dummy.{DummyNode, OrderNode, SegmentDummy}
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node.OrderNodeReadOnlyCache.OrderNodeReadOnlyCache
import metal.mutable.HashMap
import metal.syntax._

import scala.collection.mutable

class DummyConstructor(nodes: OrderNodeReadOnlyCache,
                       private var id: Int) {

  def construct(nodeLayer: HashMap[Int, HashMap[Int, OrderNode]]): Traversable[DummyNode] = {
    val work = mutable.MutableList.empty[HashMap[Int, OrderNode] => Unit]
    val outgoing = mutable.MutableList.empty[Int]

    val res = mutable.MutableList.empty[DummyNode]

    nodeLayer.foreach { (layer, nodes) =>
      nodes.foreach { (nodeId, node) =>
        outgoing ++= node.outgoing
        work += { l =>
          node.outgoing.foreach { i =>
            l.get(i).foreach { neighbour =>
              if (layer + 1 < neighbour.layer) {
                id += 1
                if (layer + 2 == neighbour.layer)
                  res += new DummyNode(id, layer + 1, nodeId, neighbour.id)
                else
                  res += new SegmentDummy(id, layer + 1, neighbour.layer - 1, nodeId, neighbour.id)
              }
            }
          }
        }
      }
    }

    val outgoingNodes = nodes.get(outgoing.toSet)
    work.foreach(_(outgoingNodes))

    res.toList
  }

}
