package com.clemaire.gexplore.core.gfa.reference

import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeCache.NodeCache
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.{GraphData, GraphHeader}
import com.clemaire.gexplore.core.gfa.reference.genome.cache.{GCCache, GCReadOnlyCache}
import com.clemaire.gexplore.core.gfa.reference.heatmap.SingleFlushHeatMapWriter
import com.clemaire.gexplore.core.gfa.reference.node.cache.NodeReadOnlyCache.NodeReadOnlyCache

import scala.collection.mutable

class ReferenceCacheBuilder(paths: CachePathList,
                            nGenomes: Int) {
  private[this] val nodes: NodeCache = NodeCache(paths.referencePath, paths.referenceIndexPath)
  private[this] val genomes: GCCache = GCCache(paths.coordinatesPath, paths.coordinatesIndexPath, nGenomes)

  private[this] val heatMapWriter: SingleFlushHeatMapWriter = new SingleFlushHeatMapWriter(paths)

  private[this] var workBuffer: mutable.Buffer[BuilderReferenceNode] = mutable.Buffer()

  def toData(header: GraphHeader): GraphData =
    new GraphData(paths, header) {
      override val nodeCache: NodeReadOnlyCache = nodes.readOnly
      override val gcCache: GCReadOnlyCache = ReferenceCacheBuilder.this.genomes.readOnly
    }

  private def doWork(): Unit = {
    nodes ++= workBuffer
    genomes ++= workBuffer

    heatMapWriter.write(workBuffer)

    workBuffer.clear()
  }

  def +=(node: BuilderReferenceNode): Unit = {
    workBuffer += node

    if (workBuffer.size > 8192)
      doWork()
  }

  def flush(): Unit = {
    doWork()

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
