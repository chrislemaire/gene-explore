package com.clemaire.gexplore.core.gfa.coordinates.crossing.algorithm.phase

import com.clemaire.gexplore.core.gfa.coordinates.crossing.algorithm.ALData
import com.clemaire.gexplore.core.gfa.coordinates.crossing.Measure
import com.clemaire.gexplore.core.gfa.coordinates.data.AlternatingNode

trait LayerMeasurer
  extends Object
    with ALData
    with Measure {

  def measure(): Unit =
    assignPositionsAndMeasures()

  def assignNodePositions(): Unit =
    first.foreach(identity)(node => {
      node.position = node.prev.map(seg =>
        seg.prev.fold(0)(_.position) + seg.size + 1).get
    })

  private def assignPositionAndMeasure(current: Option[AlternatingNode],
                                       position: Int): Unit =
    current.foreach(nodeEntry => {
      nodeEntry.position = position
      nodeEntry.measure = calculateMeasure(nodeEntry)

      nodeEntry.next.foreach(segmentEntry => {
        assignPositionAndMeasure(segmentEntry.next,
          position + segmentEntry.size + 1)
      })
    })

  private def assignPositionsAndMeasures(): Unit =
    assignPositionAndMeasure(first.next, first.size)

}
