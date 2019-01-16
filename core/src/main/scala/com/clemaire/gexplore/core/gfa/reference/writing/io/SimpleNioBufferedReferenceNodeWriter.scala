package com.clemaire.gexplore.core.gfa.reference.writing.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.cache.SimpleReferenceCacheBuilder
import com.clemaire.gexplore.core.gfa.reference.index.{GenomeCoordinateIndex, ReferenceIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.{AdditionalReferenceWriter, SingleFlushHeatMapWriter}
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.GenomeCoordinatesWriter
import com.clemaire.gexplore.core.gfa.reference.writing.index.SimpleNioBufferedReferenceIndexWriter
import com.clemaire.gexplore.util.io.{AsyncNioBufferedWriter, NioBufferedWriter}

class SimpleNioBufferedReferenceNodeWriter(paths: CachePathList,
                                           builder: SimpleReferenceCacheBuilder)
  extends AsyncNioBufferedWriter[(ReferenceNode, Int)]
    with SimpleReferenceDataWriter
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
    new SimpleNioBufferedReferenceIndexWriter(paths)

  /**
    * The [[AdditionalReferenceWriter]] responsible for
    * writing genome coordinates to file and managing the
    * building of a [[GenomeCoordinateIndex]].
    */
  private val coordinatesWriter =
    new GenomeCoordinatesWriter(paths, builder.genomeCoordinates)

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
