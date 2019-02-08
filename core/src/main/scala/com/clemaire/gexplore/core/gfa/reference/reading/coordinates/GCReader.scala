package com.clemaire.gexplore.core.gfa.reference.reading.coordinates

import java.nio.channels.FileChannel
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.cache.reader.StitchedFragmentReader
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.GCChunk
import com.clemaire.gexplore.core.gfa.reference.index.GCChunkIndex
import com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data.GCDataReader
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.data.GenomeCoordinate

class GCReader(val paths: CachePathList)
  extends StitchedFragmentReader[GCChunk, GCChunkIndex]
    with GCDataReader {

  private val _fc: FileChannel = FileChannel.open(paths.coordinatesPath)

  override protected def readFragment(start: Long,
                                      end: Long,
                                      indices: Traversable[GCChunkIndex])
  : Map[Int, GCChunk] = {
    val result: ByteBuffer = ByteBuffer
      .allocateDirect((end - start).toInt)

    _fc.read(result, start)

    indices.map(index =>
      index.id -> GCChunk(index,
        (1 to Int.MaxValue)
          .map(_ => read(result))
          .takeWhile(_ => result.position() <= index.filePos + index.length)
          .map(gc =>
            gc._1 -> GenomeCoordinate(gc._1,
              gc._2.map(kv => (kv._1, index.referenceCoordinates(kv._1) + kv._2)))
          ).toMap
      )
    ).toMap
  }

  override def close(): Unit =
    _fc.close()

}
