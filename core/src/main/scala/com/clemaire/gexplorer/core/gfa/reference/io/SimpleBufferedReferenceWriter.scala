package com.clemaire.gexplorer.core.gfa.reference.io

import java.io._
import java.nio.file.Files

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}

import scala.collection.mutable

class SimpleBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter
    with SimpleReferenceWriter {

  /**
    * The output stream to which data is written
    * in a buffered manner.
    */
  private val os = new DataOutputStream(new BufferedOutputStream(
    new FileOutputStream(paths.referenceFilePath.toFile)))

  /**
    * Index at which the next [[ReferenceNode]]
    * should be stored to buffer.
    */
  private var index: Int = 0

  /**
    * The buffer for [[ReferenceNode]]s to be written
    * to an output stream all at once.
    */
  private val nodeBuffer: mutable.Buffer[ReferenceNode] =
    mutable.Buffer.fill[ReferenceNode](8192)(ReferenceNode.empty)

  /**
    * Writes the buffered nodes to the given
    * output data stream in a blocking fashion.
    *
    * @param os The [[DataOutputStream]] to write
    *           the buffer to.
    */
  private def writeBufferTo(os: DataOutputStream): Unit =
    nodeBuffer.take(index).foreach(node =>
      write(node, os))

  /**
    * Writes the buffered nodes to a new buffered
    * output stream and resets the buffer.
    */
  private def flushBuffer(): Unit = {
    writeBufferTo(os)
    index = 0
  }

  override def write(referenceNode: ReferenceNode): Unit = {
    if (index == nodeBuffer.length) {
      flushBuffer()
    }

    nodeBuffer(index) = referenceNode
    index += 1
  }

  override def flush(): Unit = {
    flushBuffer()
    os.flush()
  }

  override def close(): Unit =
    os.close()

}
