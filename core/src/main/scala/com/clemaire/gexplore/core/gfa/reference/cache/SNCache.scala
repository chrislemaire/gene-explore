package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.specialized.NodeCache
import com.clemaire.gexplore.core.gfa.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.SNChunk
import com.clemaire.gexplore.core.gfa.reference.index.{NodeChunkIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.reference.reading.io.SNReader

class SNCache(val index: NodeIndex,
              val reader: SNReader,
              val max: Int = 25)
  extends NodeCache[SNChunk, NodeChunkIndex, StructuralNode]
    with SetNumberOfChunks[SNChunk]
    with LRU[SNChunk]
