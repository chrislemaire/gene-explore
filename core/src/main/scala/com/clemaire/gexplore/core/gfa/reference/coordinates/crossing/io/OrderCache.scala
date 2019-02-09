package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.io

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.cache.ChunkReader
import com.clemaire.gexplore.core.gfa.reference.coordinates.io.{IndexedDataCache, IndexedDataChunk, IndexedDataChunkReader}
import com.clemaire.gexplore.core.gfa.reference.coordinates.io.impl.IndexedIntDataReader
import com.clemaire.gexplore.core.gfa.reference.index.{ChunkIndex, Index}

class OrderCache(paths: CachePathList,
                 index: Index[ChunkIndex])
  extends IndexedDataCache[Int](index) {

  override val reader: ChunkReader[IndexedDataChunk[Int], ChunkIndex] =
    new IndexedDataChunkReader[Int](paths.orderPath) with IndexedIntDataReader

}
