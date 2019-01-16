package com.clemaire.gexplore.core.gfa.reference.writing.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.cache.SRCacheBuilder
import com.clemaire.gexplore.core.gfa.reference.index.{GenomeCoordinateIndex, ReferenceIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.{AdditionalReferenceWriter, SingleFlushHeatMapWriter}
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.GCWriter
import com.clemaire.gexplore.core.gfa.reference.writing.index.NioBufferedSRIndexWriter
import com.clemaire.gexplore.util.io.{AsyncNioBufferedWriter, NioBufferedWriter}

class NioBufferedSRWriter(paths: CachePathList,
                          builder: SRCacheBuilder)
  extends AsyncNioBufferedWriter[(ReferenceNode, Int)]
    with SRDataWriter
    with AdditionalReferenceWriterWorkBuffer {

  /**
    * Initializes the buffer and file channel indirectly
    * through [[NioBufferedWriter]] functions.
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
    new NioBufferedSRIndexWriter(paths)

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
    val len = length(node)
    checkForFlush(len)

    write(node, buffer)
    addWork(node -> len)
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