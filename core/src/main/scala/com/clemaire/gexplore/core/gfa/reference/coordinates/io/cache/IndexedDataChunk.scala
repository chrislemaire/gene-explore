package com.clemaire.gexplore.core.gfa.reference.coordinates.io.cache

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

case class IndexedDataChunk[D](index: ChunkIndex,
                               data: Map[Int, IndexedData[D]])
  extends Chunk[ChunkIndex, IndexedData[D]]
