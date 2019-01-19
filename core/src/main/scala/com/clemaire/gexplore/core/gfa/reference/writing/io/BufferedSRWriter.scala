package com.clemaire.gexplore.core.gfa.reference.writing.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.cache.SRCacheBuilder
import com.clemaire.gexplore.core.gfa.reference.index.{GCIndex, SRIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.{AdditionalReferenceWriter, SingleFlushHeatMapWriter}
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.GCWriter
import com.clemaire.gexplore.core.gfa.reference.writing.index.NioBufferedSRIndexWriter
import com.clemaire.gexplore.util.io.IoBufferedWriter

class BufferedSRWriter(paths: CachePathList,
                       builder: SRCacheBuilder)
  extends IoBufferedWriter
    with SRDataWriter
    with AdditionalSRWriterWorkBuffer {

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
    * writing the [[SRIndex]] to file while building
    * it constantly as well.
    */
  private val indexWriter =
    new NioBufferedSRIndexWriter(paths)

  /**
    * The [[AdditionalReferenceWriter]] responsible for
    * writing genome coordinates to file and managing the
    * building of a [[GCIndex]].
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

  override def index: SRIndex =
    indexWriter.index

  override def coordinatesIndex: GCIndex =
    coordinatesWriter.indexWriter.index
}
