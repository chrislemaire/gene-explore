package com.clemaire.gexplore.core.gfa.reference.coordinates.io.cache

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex
import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.reference.reading.io.IdentifiableDataChunkReader

abstract class IndexedDataChunkReader[D](path: Path)
  extends IdentifiableDataChunkReader[IndexedDataChunk[D], ChunkIndex, IndexedData[D]](path)
    with DataReader[IndexedData[D]] {

  protected def constructChunk(index: ChunkIndex,
                               data: Map[Int, IndexedData[D]]): IndexedDataChunk[D] =
    IndexedDataChunk(index, data)

}
