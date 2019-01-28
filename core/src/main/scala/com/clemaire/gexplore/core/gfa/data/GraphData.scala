package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.reference.index.{GCIndex, NodeIndex}

case class GraphData(referenceIndex: NodeIndex,
                     coordinateIndex: GCIndex,
                     genomes: Map[Int, String]) {
  val genomeNames: Map[String, Int] = genomes.map(_.swap)
}
