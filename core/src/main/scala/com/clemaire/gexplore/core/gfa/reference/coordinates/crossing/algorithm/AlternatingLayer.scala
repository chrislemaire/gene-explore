package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase.{LayerBuilder, LayerMeasurer, MeasuredLayerSorter, QVertexResolver}
import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.SCG
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.Node

abstract class AlternatingLayer(nodes: Traversable[Node])
  extends ALData
    with LayerBuilder
    with LayerMeasurer
    with MeasuredLayerSorter
    with QVertexResolver {

  private val _: Unit = appendLayer(nodes)

  def solveNewLayer(layer: Traversable[Node]): SCG = {
    rebuildLayer(layer)
    measure()
    sort()
    resolveQVertices()

    SCG(this)
  }

}
