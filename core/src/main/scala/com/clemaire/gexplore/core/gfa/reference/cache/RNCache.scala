package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.Cache
import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.RNChunk
import com.clemaire.gexplore.core.gfa.reference.index.{NodeChunkIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.reference.reading.io.RNReader

class RNCache(val index: NodeIndex,
              val reader: RNReader,
              val max: Int = 25)
  extends Cache[RNChunk, NodeChunkIndex]
    with SetNumberOfChunks[RNChunk]
    with LRU[RNChunk]
