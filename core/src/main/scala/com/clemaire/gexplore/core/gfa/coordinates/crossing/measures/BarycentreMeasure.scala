package com.clemaire.gexplore.core.gfa.coordinates.crossing.measures

import com.clemaire.gexplore.core.gfa.coordinates.crossing.Measure
import com.clemaire.gexplore.core.gfa.coordinates.data.AlternatingNode

class BarycentreMeasure
  extends Measure {

  override def calculateMeasure(node: AlternatingNode): Double =
    node.incoming.map(_.position).sum / node.incoming.size

}
