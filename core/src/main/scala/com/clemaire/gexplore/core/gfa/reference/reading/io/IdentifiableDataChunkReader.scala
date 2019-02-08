package com.clemaire.gexplore.core.gfa.reference.reading.io

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}

import com.clemaire.gexplore.core.gfa.cache.reader.StitchedFragmentReader
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex
import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.data.Identifiable

abstract class IdentifiableDataChunkReader[C <: Chunk[CI, D], CI <: ChunkIndex, D <: Identifiable](val path: Path)
  extends StitchedFragmentReader[C, CI]
    with DataReader[D] {

  private val _fc: FileChannel = FileChannel.open(path, StandardOpenOption.READ)

  protected[this] def constructChunk(index: CI,
                                     data: Map[Int, D]): C

  override protected def readFragment(start: Long,
                                      end: Long,
                                      indices: Traversable[CI])
  : Map[Int, C] = {
    val result: ByteBuffer = ByteBuffer
      .allocateDirect((end - start).toInt)

    _fc.read(result, start)

    indices.map(index => {
      index.id -> constructChunk(
        index,
        (1 to Int.MaxValue)
          .map(_ => this.read(result))
          .takeWhile(_ => result.hasRemaining)
          .map(data => data.id -> data)
          .toMap
      )
    }).toMap
  }


  override def close(): Unit =
    _fc.close()

}
