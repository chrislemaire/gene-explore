package com.clemaire.gexplore.core.gfa.reference.reading.io

import java.nio.channels.FileChannel
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.cache.reader.StitchedFragmentReader
import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.SNChunk
import com.clemaire.gexplore.core.gfa.reference.reading.io.data.SNDataReader

class SNReader(val paths: CachePathList)
  extends StitchedFragmentReader[SNChunk, NodeChunkIndex]
    with SNDataReader {

  private val _fc: FileChannel = FileChannel.open(paths.referencePath)

  override protected def readFragment(fragment: (Long, Long),
                                      indices: Traversable[NodeChunkIndex]): Map[Int, SNChunk] = {
    val result: ByteBuffer = ByteBuffer
      .allocateDirect((fragment._2 - fragment._1).toInt)

    _fc.read(result, fragment._1)

    indices.map(index => {
      index.id -> SNChunk(
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
