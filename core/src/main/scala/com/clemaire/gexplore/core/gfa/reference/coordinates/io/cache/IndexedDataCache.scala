package com.clemaire.gexplore.core.gfa.reference.coordinates.io.cache

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.specialized.IndexedCache
import com.clemaire.gexplore.core.gfa.reference.index.{ChunkIndex, Index}

abstract class IndexedDataCache[D](path: Path,
                                   val index: Index[ChunkIndex] = new Index(),
                                   val max: Int = 10)
  extends IndexedCache[IndexedDataChunk[D], ChunkIndex, IndexedData[D]]
    with SetNumberOfChunks[IndexedDataChunk[D]]
    with LRU[IndexedDataChunk[D]]
