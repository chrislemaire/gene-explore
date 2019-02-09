package com.clemaire.gexplore.core.gfa.reference.coordinates.io

import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.specialized.IndexedCache
import com.clemaire.gexplore.core.gfa.reference.index.{ChunkIndex, Index}

abstract class IndexedDataCache[D](val index: Index[ChunkIndex],
                                   val max: Int = 10)
  extends IndexedCache[IndexedDataChunk[D], ChunkIndex, IndexedData[D]]
    with SetNumberOfChunks[IndexedDataChunk[D]]
    with LRU[IndexedDataChunk[D]]
