package com.clemaire.gexplore.core.gfa.reference.index

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.interval.IntInterval
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.util.io.NioBufferedWriter

import SimpleBufferedReferenceIndexWriter._

object SimpleBufferedReferenceIndexWriter {
  /**
    * The size of each index chunk in segments.
    */
  private val INDEX_CHUNK_SIZE = 4096
}

class SimpleBufferedReferenceIndexWriter(val paths: CachePathList)
  extends AdditionalReferenceWriter
    with NioBufferedWriter
    with SimpleReferenceIndexWriter {

  private val _: Unit = {
    withPath(paths.referenceIndexPath)
  }

  /**
    * The in-memory representation of the [[ReferenceIndex]]
    * that is created during writing of the index to file.
    */
  private val index: ReferenceIndex = new ReferenceIndex(paths)

  /**
    * The id of the chunk currently being worked on.
    */
  private var chunkId = 0

  /**
    * The position in the file the chunk that is currently
    * being processed starts.
    */
  private var length: Int = 0

  /**
    * The position in the file the chunk that is currently
    * being processed starts.
    */
  private var filePos: Long = 0

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
    * Updates the given interval such that its range
    * contains its previous range plus the given value.
    *
    * @param interval The interval to update.
    * @param value    The value to push into the interval.
    */
  private[this] def pushBoundaries(interval: IntInterval,
                                   value: Int): Unit =
    if (value < interval.start) interval.start = value
    else if (value > interval.end) interval.end = value

  /**
    * Flushes the currently being built [[ReferenceChunkIndex]]
    * to the [[ReferenceIndex]] and the index file.
    */
  private[this] def flushIndex(): Unit = {
    checkForFlush(chunkLength)

    val chunkIndex = ReferenceChunkIndex(chunkId,
      length, filePos,
      layers.toInterval, segmentIds.toInterval)

    chunkId += 1
    length = 0
    index += chunkIndex
    layers = new IntInterval(Int.MaxValue, Int.MinValue)
    segmentIds = new IntInterval(Int.MaxValue, Int.MinValue)

    write(chunkIndex, buffer)
  }

  override def writeNode(node: ReferenceNode): Unit = {
    if (length >= INDEX_CHUNK_SIZE) {
      flushIndex()

      filePos = node.fileOffset
    }

    pushBoundaries(layers, node.layer)
    pushBoundaries(segmentIds, node.id)
    length += 1
  }

  override def flush(): Unit = {
    flushIndex()
    super.flush()
  }
}