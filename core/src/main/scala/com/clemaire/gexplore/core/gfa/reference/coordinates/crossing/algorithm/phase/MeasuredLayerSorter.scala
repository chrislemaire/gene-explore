package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.Container

import scala.collection.mutable

trait MeasuredLayerSorter
  extends Object
    with ALData {

  val vertices: mutable.ArrayStack[AlternatingNode] = mutable.ArrayStack()
  val segments: mutable.ArrayStack[Container] = mutable.ArrayStack()

  def sort(): Unit = {
    moveToLists()
    popBack()
  }

  private def moveToLists(): Unit = {
    vertices.clear()
    segments.clear()

    val nodes = mutable.Buffer[AlternatingNode]()
    val reverseSegments = mutable.Buffer[Container]()

    first.foreach(segEntry => {
      if (segEntry.size > 0)
        reverseSegments += segEntry
    })(nodeEntry => nodes += nodeEntry)

    vertices ++= nodes.sortBy(_.measure)
    segments ++= reverseSegments.reverse
  }

  private def popBack(): Unit = {
    first = new Container()
    var current: Either[Container, AlternatingNode] = Left(first)

    while (vertices.nonEmpty && segments.nonEmpty) {
      if (vertices.head.measure <= segments.head.position) {
        current = Right(current.fold(
          s => s.append(vertices.pop()),
          n => n.append(new Container()).append(vertices.pop())
        ))
      }
      if (vertices.head.measure >= (segments.head.position + segments.head.size - 1)) {
        current = current.fold(
          s => {
            s.join(segments.pop())
            current
          },
          n => Left(n.append(segments.pop()))
        )
      } else {
        val S = segments.pop()
        val v = vertices.pop()
        val k: Int = v.measure.round.toInt - S.position

        val S2 = S.split(k)

        current = Right(current.fold(
          s => {
            s.join(S)
            s.append(v)
          },
          n => n.append(S).append(v)
        ))

        S2.position = S.position + k
        segments.push(S2)
      }
    }

    last = current.fold(
      s => s,
      n => n.append(new Container())
    )

    while (vertices.nonEmpty) {
      last = last.append(vertices.pop()).append(new Container())
    }
    while (segments.nonEmpty) {
      last.join(segments.pop())
    }

    last.next = None
  }

}
