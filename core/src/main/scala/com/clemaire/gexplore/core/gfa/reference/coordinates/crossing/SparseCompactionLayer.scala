package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.{AlternatingNode, Segment}

import scala.collection.mutable

case class SparseCompactionLayer private(order: Traversable[Either[AlternatingNode, Segment]])

object SparseCompactionLayer {

  def apply(al: ALData): SparseCompactionLayer = {
    val buffer = mutable.Buffer[Either[AlternatingNode, Segment]]()

    al.first.foreach(c => {
      if (c.size > 0)
        buffer += Right(c.first.get)
      if (c.size > 1)
        buffer += Right(c.last.get)
    })(n => {
      buffer += Left(n)
    })

    SparseCompactionLayer(buffer)
  }

}
