package com.clemaire.gexplore.core.gfa.reference.reading.io

import java.nio.channels.FileChannel
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.cache.reader.StitchedFragmentReader
import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.RNChunk
import com.clemaire.gexplore.core.gfa.reference.reading.io.data.RNDataReader

class RNReader(val paths: CachePathList)
  extends StitchedFragmentReader[RNChunk, NodeChunkIndex]
    with RNDataReader {

  private val _fc: FileChannel = FileChannel.open(paths.referencePath)

  override protected def readFragment(fragment: (Long, Long),
                                      indices: Traversable[NodeChunkIndex])
  : Map[Int, RNChunk] = {
    val result: ByteBuffer = ByteBuffer
      .allocateDirect((fragment._2 - fragment._1).toInt)

    _fc.read(result, fragment._1)

    indices.map(index => {
      index.id -> RNChunk(
        index,
        (1 to Int.MaxValue)
          .map(_ => this.read(result))
          .takeWhile(_ => result.hasRemaining)
          .map(node => node.id -> node)
          .toMap
      )
    }).toMap
  }

  override def close(): Unit =
    _fc.close()

}
