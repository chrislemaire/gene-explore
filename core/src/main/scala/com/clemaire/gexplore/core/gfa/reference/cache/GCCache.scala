package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.specialized.IndexedCache
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.GCChunk
import com.clemaire.gexplore.core.gfa.reference.data.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.index.{GCChunkIndex, GCIndex}
import com.clemaire.gexplore.core.gfa.reference.reading.coordinates.GCReader
import com.clemaire.gexplore.core.gfa.CachePathList

class GCCache(val paths: CachePathList,
              val index: GCIndex,
              val max: Int = 20)
  extends IndexedCache[GCChunk, GCChunkIndex, GenomeCoordinate]
    with SetNumberOfChunks[GCChunk]
    with LRU[GCChunk] {
  val reader: GCReader = new GCReader(paths)
}
