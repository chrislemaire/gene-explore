package com.clemaire.gexplore.core.gfa.reference.writing.additional

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.data.BuilderReferenceNode
import com.clemaire.gexplore.util.io.NioBufferedWriter

import scala.collection.mutable

class SingleFlushHeatMapWriter(val paths: CachePathList)
  extends AdditionalReferenceWriter
    with NioBufferedWriter {

  private val _: Unit = {
    withPath(paths.heatMapPath)
  }

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

  override def writeNode(node: BuilderReferenceNode,
                         byteLength: Int): Unit =
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
