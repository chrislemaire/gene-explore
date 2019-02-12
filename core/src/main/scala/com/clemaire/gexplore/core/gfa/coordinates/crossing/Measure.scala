package com.clemaire.gexplore.core.gfa.coordinates.crossing

import com.clemaire.gexplore.core.gfa.coordinates.data.AlternatingNode

trait Measure {
  def calculateMeasure(node: AlternatingNode): Double
}
