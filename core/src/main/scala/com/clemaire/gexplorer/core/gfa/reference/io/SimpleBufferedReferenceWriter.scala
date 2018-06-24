package com.clemaire.gexplorer.core.gfa.reference.io

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{IoBufferedWriter, ReferenceNode, ReferenceWriter}

class SimpleBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter
    with SimpleReferenceWriter
    with IoBufferedWriter {

  /**
    * Initializes the writer by setting the path for
    * the [[IoBufferedWriter]] to write to.
    */
  private val _: Unit = {
    withPath(paths.referencePath)
  }

  override def write(referenceNode: ReferenceNode): Unit = {
    write(referenceNode, os)
  }

}
