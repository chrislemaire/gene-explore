package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.{Cache, Chunk}
import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.{NodeChunkIndex, NodeIndex}
import com.clemaire.gexplore.core.gfa.reference.reading.io.RNReader

case class RNCache(index: NodeIndex,
                   reader: RNReader,
                   max: Int = 25)
  extends Cache[NodeIndex, NodeChunkIndex, ReferenceNode]
    with SetNumberOfChunks[NodeChunkIndex, ReferenceNode]
    with LRU[Chunk[NodeChunkIndex, ReferenceNode]]
