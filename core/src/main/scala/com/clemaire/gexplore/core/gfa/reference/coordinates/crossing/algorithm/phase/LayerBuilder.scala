package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.Container

import scala.collection.mutable

trait LayerBuilder
  extends Object
    with ALData {

  private val pVertices: mutable.Buffer[AlternatingNode] = mutable.Buffer()

  private def appendToLast(node: AlternatingNode): Unit =
    last = last.append(node).append(new Container())

  def buildLayer(layer: mutable.ArrayStack[AlternatingNode]): Unit = {
    var curr = first.next
    while (layer.nonEmpty) {
      val next = layer.pop()
      if (next.isQNode)
        qVertices += next

      curr.fold[Unit](appendToLast(next))(node => {
        if (node.isPNode) {
          node.prev.get.append(node.segment)
          node.remove()
        }

        node.replace(next)
        curr = node.next.flatMap(_.next)
      })
    }

    while (curr.isDefined){
      curr.get.remove()
      curr = curr.flatMap(_.next).flatMap(_.next)
    }
  }

}
