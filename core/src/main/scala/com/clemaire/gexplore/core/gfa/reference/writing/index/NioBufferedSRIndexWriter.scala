package com.clemaire.gexplore.core.gfa.reference.writing.index

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.interval.IntInterval
import com.clemaire.gexplore.core.gfa.reference.index.{SRChunkIndex, SRIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.util.io.NioBufferedWriter
import NioBufferedSRIndexWriter._
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode

object NioBufferedSRIndexWriter {
  /**
    * The size of each index chunk in bytes.
    */
  private val MAX_CHUNK_LENGTH = 1024 * 1024
}

class NioBufferedSRIndexWriter(val paths: CachePathList)
  extends AdditionalReferenceWriter
    with NioBufferedWriter
    with SRIndexDataWriter {

  private val _: Unit = {
    withPath(paths.referenceIndexPath)
  }

  /**
    * The in-memory representation of the [[SRIndex]]
    * that is created during writing of the index to file.
    */
  val index: SRIndex = new SRIndex()

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
    * Flushes the currently being built [[SRChunkIndex]]
    * to the [[SRIndex]] and the index file.
    */
  private[this] def flushIndex(): Unit = {
    checkForFlush(LENGTH)

    val chunkIndex = SRChunkIndex(chunkId,
      filePos, bytesWritten,
      layers.toInterval, segmentIds.toInterval)

    index += chunkIndex

    chunkId += 1
    nodesProcessed = 0
    bytesWritten = 0

    layers = new IntInterval(Int.MaxValue, Int.MinValue)
    segmentIds = new IntInterval(Int.MaxValue, Int.MinValue)

    write(chunkIndex, buffer)
  }

  override def writeNode(node: ReferenceNode,
                         byteLength: Int): Unit = {
    if (bytesWritten >= MAX_CHUNK_LENGTH) {
      flushIndex()

      filePos = node.fileOffset
    }

    layers.pushBoundaries(node.layer)
    segmentIds.pushBoundaries(node.id)

    nodesProcessed += 1
    bytesWritten += byteLength
  }

  override def flush(): Unit = {
    flushIndex()
    super.flush()
  }
}
