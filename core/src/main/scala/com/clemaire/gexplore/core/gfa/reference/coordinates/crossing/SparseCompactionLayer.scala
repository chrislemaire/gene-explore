package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData

import scala.collection.mutable

object SparseCompactionLayer {

  def apply(al: ALData): Traversable[Int] = {
    val buffer = mutable.Buffer[Int]()

    al.first.foreach(c => {
      if (c.size > 0)
        buffer += c.first.get.id
      if (c.size > 1)
        buffer += c.last.get.id
    })(n => {
      buffer += n.id
    })

    buffer
  }

}
