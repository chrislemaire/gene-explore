package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Node

trait Measure {
  def calculateMeasure(node: Node): Double
}
