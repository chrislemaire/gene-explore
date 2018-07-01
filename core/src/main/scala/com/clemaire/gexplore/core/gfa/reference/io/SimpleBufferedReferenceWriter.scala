package com.clemaire.gexplore.core.gfa.reference.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.additional.SingleFlushHeatMapWriter
import com.clemaire.gexplore.core.gfa.reference.cache.SimpleReferenceBuilder
import com.clemaire.gexplore.core.gfa.reference.coordinates.GenomeCoordinatesWriter
import com.clemaire.gexplore.core.gfa.reference.index.SimpleBufferedReferenceIndexWriter
import com.clemaire.gexplore.util.io.IoBufferedWriter

class SimpleBufferedReferenceWriter(paths: CachePathList,
                                    builder: SimpleReferenceBuilder)
  extends IoBufferedWriter
    with SimpleReferenceDataWriter
    with AdditionalReferenceWriterWorkBuffer {

  /**
    * Initializes the writer by setting the path for
    * the [[IoBufferedWriter]] to write to.
    */
  private val _: Unit = {
    withPath(paths.referencePath)
  }

  override protected[this] val additionalWriters = Seq(
    new SingleFlushHeatMapWriter(paths),
    new SimpleBufferedReferenceIndexWriter(paths),
    new GenomeCoordinatesWriter(paths, builder.genomeCoordinates)
  )

  override def write(node: ReferenceNode): Unit = {
    write(node, os)
    additionalWriters.foreach(_.writeNode(node, length(node)))
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
