package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Identifiable

case class SCG private(relations: Traversable[(Identifiable, Identifiable)])

object SCG {

  def apply(al: ALData): SCG = {
    SCG(al.first.map[(Identifiable, Identifiable)](c => {
      if (c.size > 0)
        c.last.get -> c.next.orNull
      else
        null -> null
    })(n => {
      if (n.next.get.size > 0)
        n -> n.next.get.first.get
      else
        n -> n.next.flatMap(_.next).orNull
    }).filterNot(_._1 == null).filterNot(_._2 == null))
  }

}
