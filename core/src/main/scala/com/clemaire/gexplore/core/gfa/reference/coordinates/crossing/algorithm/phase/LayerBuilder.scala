package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Node
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.Container

import scala.collection.mutable

trait LayerBuilder
  extends Object
    with ALData {

  private val pVertices: mutable.Buffer[Node] = mutable.Buffer()

  def appendLayer(layer: Traversable[Node]): Unit =
    layer.foreach(node => {
      if (node.isQNode) {
        qVertices += node
      } else {
        last = last
          .append(node)
          .append(new Container())
      }
    })

  private def trimTrailingNodes(current: Node): Unit = {
    if (current.isPNode) {
      pVertices += current
    } else {
      current.remove()
    }
    current.next.flatMap(_.next)
      .foreach(trimTrailingNodes)
  }

  private def insertLayer(layer: Traversable[Node],
                  current: Option[Node] = first.next): Unit =
    if (layer.nonEmpty) {
      if (current.exists(_.isPNode)) {
        pVertices += current.get
        insertLayer(layer, current.flatMap(_.next).flatMap(_.next))
      } else if (layer.head.isQNode) {
        qVertices += layer.head
        insertLayer(layer.tail, current)
      } else if (current.nonEmpty) {
        current.get.replace(layer.head)
        insertLayer(layer.tail, current.flatMap(_.next).flatMap(_.next))
      } else {
        last = current.get.append(new Container())
        appendLayer(layer)
      }
    } else if (current.nonEmpty) {
      trimTrailingNodes(current.get)
    }

  def rebuildLayer(layer: Traversable[Node]): Unit = {
    insertLayer(layer, first.next)

    pVertices.foreach(p => {
      p.prev.get.append(p.segment)
      p.remove()
    })
    pVertices.clear()
  }

}
