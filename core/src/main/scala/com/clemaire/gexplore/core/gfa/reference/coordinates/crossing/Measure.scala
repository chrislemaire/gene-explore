package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode

trait Measure {
  def calculateMeasure(node: AlternatingNode): Double
}
