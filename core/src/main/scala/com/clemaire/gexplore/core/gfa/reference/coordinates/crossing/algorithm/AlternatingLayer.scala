package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.algorithm.phase.{LayerBuilder, LayerMeasurer, MeasuredLayerSorter, QVertexResolver}
import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.SparseCompactionLayer
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.AlternatingNode
import com.clemaire.gexplore.core.gfa.reference.coordinates.io.IndexedDataWriter

import scala.collection.mutable

abstract class AlternatingLayer(nodes: mutable.ArrayStack[AlternatingNode])
  extends ALData
    with LayerBuilder
    with LayerMeasurer
    with MeasuredLayerSorter
    with QVertexResolver {

  private val _: Unit = buildLayer(nodes)

  /**
    * Constructs a complete representation of the given layer,
    * applies the measure on that layer and finally returns the
    * Sparse Compaction Graph-layer for that layer.
    *
    * Follows the methods and algorithms defined and described by
    * Eiglsperger, Siebenhaller and Kaufmann in
    * "An Efficient Implementation of Sugiyama's Algorithm for Layered Graph Drawing".
    *
    * @see http://emis.math.tifr.res.in/journals/JGAA/accepted/2005/EiglspergerSiebenhallerKaufmann2005.9.3.pdf
    *
    * @param layer The layer to solve for.
    * @return The sparse compaction layer defining the order of
    *         elements in that layer.
    */
  def solveNewLayer(layer: mutable.ArrayStack[AlternatingNode],
                    writer: IndexedDataWriter[Int]): SparseCompactionLayer = {
    buildLayer(layer)
    measure()
    sort()
    resolveQVertices()
    assignNodePositions()

    writer.write(layer)
  }

}
