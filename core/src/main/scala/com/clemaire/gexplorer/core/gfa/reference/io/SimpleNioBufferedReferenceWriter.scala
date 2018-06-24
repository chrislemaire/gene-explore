package com.clemaire.gexplorer.core.gfa.reference.io

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{NioBufferedWriter, ReferenceNode, ReferenceWriter}

class SimpleNioBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter
    with SimpleReferenceWriter
    with NioBufferedWriter {

  /**
    * Initializes the buffer and file channel indirectly
    * through [[NioBufferedWriter]] functions.
    */
  private val _: Unit = {
    withBufferSize(1024 * 1024 * 8)
    withPath(paths.referencePath)
  }

  override def write(node: ReferenceNode): Unit = {
    checkForFlush(length(node))

    write(node, buffer)
  }
}
