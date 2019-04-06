package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.genome.cache.GCReadOnlyCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeReadOnlyCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeReadOnlyCache.NodeReadOnlyCache

class GraphData(val paths: CachePathList,
                val header: GraphHeader) {
  val headerOptions: Map[String, String] = header.options

  val genomes: Map[Int, String] = header.genomes
  val genomeNames: Map[String, Int] = genomes.map(_.swap)
  val genomeMaxes: Map[Int, Long] = header.coordinateMax

  val nodeCache: NodeReadOnlyCache = NodeReadOnlyCache(paths.referencePath, paths.referenceIndexPath)
  val gcCache: GCReadOnlyCache = new GCReadOnlyCache(paths.coordinatesPath, paths.coordinatesIndexPath, genomes.size)

  val secondary: SecondaryGraphData = new SecondaryGraphData(paths, this)
}
