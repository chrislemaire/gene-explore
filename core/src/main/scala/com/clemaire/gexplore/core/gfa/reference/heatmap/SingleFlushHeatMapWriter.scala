package com.clemaire.gexplore.core.gfa.reference.heatmap

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.reference.BuilderReferenceNode
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.io.writing.NioBufferedWriter

import scala.collection.mutable

class SingleFlushHeatMapWriter(val path: Path)
  extends NioBufferedWriter {

  def this(paths: CachePathList) = this(paths.heatMapPath)

  /**
    * The size of the entry that is written to disk.
    */
  private val ENTRY_SIZE = 8

  /**
    * The nodes-per-layer map, mapping a layer identifier
    * to the number of nodes that were found to be in that
    * layer up until now.
    */
  private val nodesPerLayer: mutable.Map[Int, Int] =
    mutable.HashMap()

  def write(node: BuilderReferenceNode): Unit =
    if (nodesPerLayer.contains(node.layer)) {
      nodesPerLayer(node.layer) += 1
    } else {
      nodesPerLayer(node.layer) = 1
    }

  override def flush(): Unit = {
    nodesPerLayer.foreach(npl => {
      if (buffer.position() + ENTRY_SIZE >= buffer.capacity()) {
        flushBuffer()
      }

      buffer.putInt(npl._1)
      buffer.putInt(npl._2)
    })
    super.flush()
  }

}
