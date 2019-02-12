package com.clemaire.gexplore.core.gfa.coordinates.crossing.algorithm

import com.clemaire.gexplore.core.gfa.coordinates.data.splay.Container
import com.clemaire.gexplore.core.gfa.coordinates.data.AlternatingNode

import scala.collection.mutable

trait ALData {

  var first: Container = new Container()
  var last: Container = first

  val qVertices: mutable.Buffer[AlternatingNode] = mutable.Buffer()

}
