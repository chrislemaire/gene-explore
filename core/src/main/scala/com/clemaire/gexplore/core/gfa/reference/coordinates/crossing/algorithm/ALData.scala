package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.Container
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode

import scala.collection.mutable

trait ALData {

  var first: Container = new Container()
  var last: Container = first

  val qVertices: mutable.Buffer[AlternatingNode] = mutable.Buffer()

}
