package com.clemaire.gexplorer.core.gfa.reference.io

import java.io._
import java.nio.file.Files

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}

import scala.collection.mutable

class SimpleBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter {

  private var index: Int = 0

  private val nodeBuffer: mutable.Buffer[ReferenceNode] =
    mutable.Buffer.fill[ReferenceNode](1024)(ReferenceNode.empty)

  private def writeBufferTo(os: DataOutputStream): Unit = {
    nodeBuffer.take(index).foreach(node => {
      os.writeInt(node.id)
      os.writeLong(node.fileOffset)
      os.writeInt(node.contentLength)
      os.writeInt(node.outgoingEdges.length)
      os.writeInt(node.incomingEdges.length)

      node.outgoingEdges.foreach(kv => {
        os.writeInt(kv._1)
        os.writeLong(kv._2)
      })
      node.incomingEdges.foreach(kv => {
        os.writeInt(kv._1)
        os.writeLong(kv._2)
      })
    })
  }

  private def writeBuffer(): Unit = {
    val os = new DataOutputStream(
      Files.newOutputStream(paths.referenceFilePath))

    writeBufferTo(os)

    index = 0
  }

  override def write(referenceNode: ReferenceNode): Unit = {
    if (index == nodeBuffer.length) {
      writeBuffer()
    }

    nodeBuffer(index) = referenceNode
    index += 1
  }

  override def flush(): Unit =
    writeBuffer()

  override def close(): Unit = {}

}
