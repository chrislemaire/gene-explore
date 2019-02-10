package com.clemaire.gexplore.core.gfa.reference

import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache.NodeCache
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.{GraphData, GraphHeader}
import com.clemaire.gexplore.core.gfa.reference.genome.cache.{GCCache, GCReadOnlyCache}
import com.clemaire.gexplore.core.gfa.reference.heatmap.SingleFlushHeatMapWriter
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeReadOnlyCache.NodeReadOnlyCache

class ReferenceCacheBuilder(paths: CachePathList,
                            nGenomes: Int) {
  private[this] val nodes: NodeCache = NodeCache(paths.referencePath, paths.referenceIndexPath)
  private[this] val genomes: GCCache = GCCache(paths.coordinatesPath, paths.coordinatesIndexPath, nGenomes)

  private[this] val heatMapWriter: SingleFlushHeatMapWriter = new SingleFlushHeatMapWriter(paths)

  def toData(header: GraphHeader): GraphData =
    new GraphData(paths, header) {
      override val nodeCache: NodeReadOnlyCache = nodes.readOnly
      override val gcCache: GCReadOnlyCache = ReferenceCacheBuilder.this.genomes.readOnly
    }

  def +=(node: BuilderReferenceNode): Unit = {
    nodes += node
    genomes += node

    heatMapWriter.write(node)
  }

  def flush(): Unit = {
    nodes.flush()
    genomes.flush()

    heatMapWriter.flush()
  }

  def close(): Unit = {
    nodes.close()
    genomes.close()

    heatMapWriter.close()
  }

}
