package com.clemaire.gexplorer.core.gfa.reference.io

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{NioBufferedWriter, ReferenceNode}
import com.clemaire.gexplorer.core.gfa.reference.additional.{AdditionalReferenceWriter, SingleFlushHeatMapWriter}
import com.clemaire.gexplorer.core.gfa.reference.index.SimpleBufferedReferenceIndexWriter

class SimpleNioBufferedReferenceWriterWith(paths: CachePathList)
  extends NioBufferedWriter
    with SimpleReferenceWriter
    with AdditionalReferenceWriters {

  /**
    * Initializes the buffer and file channel indirectly
    * through [[NioBufferedWriter]] functions.
    */
  private val _: Unit = {
    withBufferSize(1024 * 1024 * 8)
    withPath(paths.referencePath)
  }

  override val additionalWriters: Seq[AdditionalReferenceWriter] = Seq(
    new SingleFlushHeatMapWriter(paths),
    new SimpleBufferedReferenceIndexWriter(paths)
  )

  override def write(node: ReferenceNode): Unit = {
    checkForFlush(length(node))
    write(node, buffer)

    additionalWriters.foreach(_.writeNode(node))
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
