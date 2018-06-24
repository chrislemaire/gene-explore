package com.clemaire.gexplorer.core.gfa.reference.additional

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{NioBufferedWriter, ReferenceNode}

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

  override def writeNode(node: ReferenceNode): Unit =
    if (nodesPerLayer.contains(node.layer)) {
      nodesPerLayer(node.layer) += 1
    } else {
      nodesPerLayer(node.layer) = 1
    }

  override def flush(): Unit = {
    var index = 0
    while (index < nodesPerLayer.size) {
      val length = (bufferSize / ENTRY_SIZE).floor.toInt
      nodesPerLayer.view
        .slice(index, index + length)
        .foreach(kv => {
          buffer.putInt(kv._1)
          buffer.putInt(kv._2)
        })
      flushBuffer()

      index += length
    }
    nodesPerLayer.clear()
    super.flush()
  }

}
