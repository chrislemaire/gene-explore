package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.specialized.NodeCache
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.RNChunk
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.{NodeChunkIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.reference.reading.io.RNReader
import com.clemaire.gexplore.core.gfa.CachePathList

class RNCache(val paths: CachePathList,
              val index: NodeIndex,
              val max: Int = 25)
  extends NodeCache[RNChunk, NodeChunkIndex, ReferenceNode]
    with SetNumberOfChunks[RNChunk]
    with LRU[RNChunk] {
  val reader: RNReader = new RNReader(paths)
}
