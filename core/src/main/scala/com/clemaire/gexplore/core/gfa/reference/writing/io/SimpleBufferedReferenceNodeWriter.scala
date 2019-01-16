package com.clemaire.gexplore.core.gfa.reference.writing.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.cache.SimpleReferenceCacheBuilder
import com.clemaire.gexplore.core.gfa.reference.index.{GenomeCoordinateIndex, ReferenceIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.{AdditionalReferenceWriter, SingleFlushHeatMapWriter}
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.GCWriter
import com.clemaire.gexplore.core.gfa.reference.writing.index.SimpleNioBufferedReferenceIndexWriter
import com.clemaire.gexplore.util.io.IoBufferedWriter

class SimpleBufferedReferenceNodeWriter(paths: CachePathList,
                                        builder: SimpleReferenceCacheBuilder)
  extends IoBufferedWriter
    with SimpleReferenceDataWriter
    with AdditionalReferenceWriterWorkBuffer {

  /**
    * Initializes the writer by setting the path for
    * the [[IoBufferedWriter]] to write to.
    */
  private val _: Unit = {
    withBufferSize(4 * 1024 * 1024)
    withPath(paths.referencePath)
  }

  /**
    * The [[AdditionalReferenceWriter]] responsible for
    * writing the [[ReferenceIndex]] to file while building
    * it constantly as well.
    */
  private val indexWriter =
    new SimpleNioBufferedReferenceIndexWriter(paths)

  /**
    * The [[AdditionalReferenceWriter]] responsible for
    * writing genome coordinates to file and managing the
    * building of a [[GenomeCoordinateIndex]].
    */
  private val coordinatesWriter =
    new GCWriter(paths, builder.genomeCoordinates)

  override protected[this] val additionalWriters: Seq[AdditionalReferenceWriter] =
    Seq(
      new SingleFlushHeatMapWriter(paths),
      indexWriter,
      coordinatesWriter
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

  override def index: ReferenceIndex =
    indexWriter.index

  override def coordinatesIndex: GenomeCoordinateIndex =
    coordinatesWriter.indexWriter.index
}
