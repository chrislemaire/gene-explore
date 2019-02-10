package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.genome.cache.GCCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache.NodeCache

case class GraphData(paths: CachePathList,
                     header: GraphHeader) {
  val headerOptions: Map[String, String] = header.options

  val genomes: Map[Int, String] = header.genomes
  val genomeNames: Map[String, Int] = genomes.map(_.swap)
  val genomeMaxes: Map[Int, Long] = header.coordinateMax

  val nodeCache: NodeCache = NodeCache(paths.referencePath, paths.referenceIndexPath)
  val gcCache: GCCache = GCCache(paths.coordinatesPath, paths.coordinatesIndexPath, genomes.size)
}
