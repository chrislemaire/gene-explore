package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.reference.index.{GCIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.CachePathList

case class GraphData(paths: CachePathList,
                     referenceIndex: NodeIndex,
                     coordinateIndex: GCIndex,
                     genomes: Map[Int, String]) {
  val genomeNames: Map[String, Int] = genomes.map(_.swap)
}
