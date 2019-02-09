package com.clemaire.gexplore.core.gfa.reference.coordinates.io

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.{DataWriter, StaticLength}
import com.clemaire.gexplore.core.gfa.interval.IntInterval
import com.clemaire.gexplore.core.gfa.reference.coordinates.io.IndexedDataIndexWriter._
import com.clemaire.gexplore.core.gfa.reference.index.{ChunkIndex, Index, NodeChunkIndex, NodeIndex}
import com.clemaire.gexplore.util.io.NioBufferedWriter

object IndexedDataIndexWriter {
  /**
    * The size of each index chunk in bytes.
    */
  private val MAX_CHUNK_LENGTH = 1024 * 1024
}

abstract class IndexedDataIndexWriter(val index: Index[ChunkIndex],
                                      val path: Path)
  extends NioBufferedWriter
    with DataWriter[ChunkIndex]
    with StaticLength {

  private val _: Unit = {
    withPath(path)
  }

  /**
    * The id of the chunk currently being worked on.
    */
  private var chunkId: Int = 0

  /**
    * The number of nodes currently processed into the chunk.
    */
  private var nodesProcessed: Int = 0

  /**
    * The position in the file the chunk that is currently
    * being processed starts.
    */
  private var filePos: Long = 0

  /**
    * The number of bytes written to disk for the nodes the
    * currently being processed chunk references.
    */
  private var bytesWritten: Int = 0

  /**
    * The interval containing the range of layers in the
    * chunk that is currently being built.
    */
  private var layers: IntInterval = new IntInterval(Int.MaxValue, Int.MinValue)

  /**
    * The interval containing the range of segment IDs
    * in the chunk that is currently being built.
    */
  private var segmentIds: IntInterval = new IntInterval(Int.MaxValue, Int.MinValue)

  /**
    * Flushes the currently being built [[NodeChunkIndex]]
    * to the [[NodeIndex]] and the index file.
    */
  private[this] def flushIndex(): Unit = {
    checkForFlush(LENGTH)

    val chunkIndex = NodeChunkIndex(chunkId,
      filePos, bytesWritten,
      layers.toInterval, segmentIds.toInterval)

    index += chunkIndex

    chunkId += 1
    nodesProcessed = 0
    filePos += bytesWritten
    bytesWritten = 0

    layers = new IntInterval(Int.MaxValue, Int.MinValue)
    segmentIds = new IntInterval(Int.MaxValue, Int.MinValue)

    write(chunkIndex, buffer)
  }

  def write(data: IndexedData[_],
            layer: Int,
            byteLength: Int): Unit = {
    if (bytesWritten >= MAX_CHUNK_LENGTH) {
      flushIndex()
    }

    layers.pushBoundaries(layer)
    segmentIds.pushBoundaries(data.id)

    nodesProcessed += 1
    bytesWritten += byteLength
  }

  override def flush(): Unit = {
    flushIndex()
    super.flush()
  }
}
