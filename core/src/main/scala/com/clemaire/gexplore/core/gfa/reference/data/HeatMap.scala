package com.clemaire.gexplore.core.gfa.reference.data

import scala.language.implicitConversions

object HeatMap {
  type HeatMap = Map[Int, Int]

  implicit def toMap(map: HeatMap): Map[Int, Int] = map
  implicit def fromMap(data: Map[Int, Int]): HeatMap = data
}
