package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.reference.index.{GCIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.cache.{GCCache, RNCache, SNCache}

case class GraphData(paths: CachePathList,
                     referenceIndex: NodeIndex,
                     coordinateIndex: GCIndex,
                     header: GraphHeader) {
  val headerOptions: Map[String, String] = header.options

  val genomes: Map[Int, String] = header.genomes
  val genomeNames: Map[String, Int] = genomes.map(_.swap)
  val genomeMaxes: Map[Int, Long] = header.coordinateMax

  val snCache = new SNCache(paths, referenceIndex)
  val rnCache = new RNCache(paths, referenceIndex)
  val gcCache = new GCCache(paths, coordinateIndex)
}
