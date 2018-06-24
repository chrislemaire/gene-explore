package com.clemaire.gexplorer.core.gfa.reference.io

import java.io._
import java.nio.file.Files

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}

class SimpleBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter
    with SimpleReferenceWriter {

  /**
    * The output stream to which data is written
    * in a buffered manner.
    */
  private val os = new DataOutputStream(new BufferedOutputStream(
    Files.newOutputStream(paths.referenceFilePath)))

  override def write(referenceNode: ReferenceNode): Unit = {
    write(referenceNode, os)
  }

  override def flush(): Unit =
    os.flush()

  override def close(): Unit =
    os.close()

}
