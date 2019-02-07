package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.Measure
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Node

trait LayerMeasurer
  extends Object
    with ALData
    with Measure {

  def measure(): Unit = {
    assignPositions()
  }

  private def assignPositionAndMeasure(current: Option[Node],
                                       position: Int): Unit =
    current.foreach(nodeEntry => {
      nodeEntry.position = position
      nodeEntry.measure = calculateMeasure(nodeEntry)

      nodeEntry.next.foreach(segmentEntry => {
        segmentEntry.position =
          if (segmentEntry.size == 0) -1
          else nodeEntry.position + 1

        assignPositionAndMeasure(segmentEntry.next,
          position + segmentEntry.size + 1)
      })
    })

  private def assignPositions(): Unit = {
    if (first.size > 0) {
      first.position = 0
    } else {
      first.position = -1
    }

    assignPositionAndMeasure(first.next, first.size)
  }

}
