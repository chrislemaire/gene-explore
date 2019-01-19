package com.clemaire.gexplore.core.gfa.reference.writing.coordinates

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.interval.IntInterval
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.{GCChunkIndex, GCIndex}
import com.clemaire.gexplore.core.gfa.reference.writing.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.GCIndexWriter._
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.data.GCIndexDataWriter
import com.clemaire.gexplore.util.io.NioBufferedWriter

import scala.collection.mutable

object GCIndexWriter {
  val MAX_CHUNK_LENGTH: Long = 1024 * 1024
}

class GCIndexWriter(paths: CachePathList,
                    val lastIndexedCoordinates: mutable.Map[Int, Long],
                    val currentCoordinates: mutable.Map[Int, Long])
  extends AdditionalReferenceWriter
    with NioBufferedWriter
    with GCIndexDataWriter {

  private val _: Unit = {
    withPath(paths.coordinatesIndexPath)
  }

  /**
    * The index keeping track of genome coordinate chunks.
    */
  val index: GCIndex = new GCIndex()

  /**
    * The current chunk ID.
    */
  private var chunkId: Int = 0

  /**
    * The file position where the first set of genome
    * coordinates is written for current index chunk.
    */
  private var filePos: Long = 0L

  /**
    * The number of bytes written to file for the current
    * genome coordinates index.
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

  def flushIndex(): Unit = {
    currentCoordinates.foreach(kv => lastIndexedCoordinates(kv._1) = kv._2)

    val chunkIndex = GCChunkIndex(chunkId, filePos, bytesWritten, layers.toInterval, segmentIds.toInterval, lastIndexedCoordinates.toMap)

    chunkId += 1
    bytesWritten = 0

    layers = new IntInterval(Int.MaxValue, Int.MinValue)
    segmentIds = new IntInterval(Int.MaxValue, Int.MinValue)

    index += chunkIndex

    checkForFlush(length(chunkIndex))
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

    bytesWritten += byteLength
  }

  override def flush(): Unit = {
    flushIndex()
    super.flush()
  }

}
