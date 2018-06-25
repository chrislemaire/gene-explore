package com.clemaire.gexplore.core.gfa.reference.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.additional.SingleFlushHeatMapWriter
import com.clemaire.gexplore.core.gfa.reference.index.SimpleBufferedReferenceIndexWriter
import com.clemaire.gexplore.util.io.{AsyncNioBufferedWriter, NioBufferedWriter}

class SimpleNioBufferedReferenceWriterWith(paths: CachePathList)
  extends AsyncNioBufferedWriter[ReferenceNode]
    with SimpleReferenceWriter
    with AdditionalReferenceWriters {

  /**
    * Initializes the buffer and file channel indirectly
    * through [[NioBufferedWriter]] functions.
    */
  private val _: Unit = {
    withBufferSize(1024 * 1024 * 4)
    withPath(paths.referencePath)
  }

  override protected[this] val additionalWriters = Seq(
    new SingleFlushHeatMapWriter(paths),
    new SimpleBufferedReferenceIndexWriter(paths)
  )

  override def write(node: ReferenceNode): Unit = {
    checkForFlush(length(node))
    write(node, buffer)
    addWork(node)
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
