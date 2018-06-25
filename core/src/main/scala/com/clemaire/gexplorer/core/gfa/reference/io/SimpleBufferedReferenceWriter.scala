package com.clemaire.gexplorer.core.gfa.reference.io

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{IoBufferedWriter, ReferenceNode}
import com.clemaire.gexplorer.core.gfa.reference.additional.SingleFlushHeatMapWriter
import com.clemaire.gexplorer.core.gfa.reference.index.SimpleBufferedReferenceIndexWriter

class SimpleBufferedReferenceWriter(paths: CachePathList)
  extends IoBufferedWriter
    with SimpleReferenceWriter
    with AdditionalReferenceWriters {

  /**
    * Initializes the writer by setting the path for
    * the [[IoBufferedWriter]] to write to.
    */
  private val _: Unit = {
    withPath(paths.referencePath)
  }

  override protected[this] val additionalWriters = Seq(
    new SingleFlushHeatMapWriter(paths),
    new SimpleBufferedReferenceIndexWriter(paths)
  )

  override def write(referenceNode: ReferenceNode): Unit = {
    write(referenceNode, os)
    additionalWriters.foreach(_.writeNode(referenceNode))
  }

  override def flush(): Unit = {
    super.flush()
    additionalWriters.foreach(_.flush())
  }

  override def close(): Unit = {
    super.close()
    additionalWriters.foreach(_.close())
  }

}
