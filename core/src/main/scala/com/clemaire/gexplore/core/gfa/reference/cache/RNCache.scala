package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.cache.{Cache, Chunk, ChunkReader}
import com.clemaire.gexplore.core.gfa.cache.capacity.SetNumberOfChunks
import com.clemaire.gexplore.core.gfa.cache.scheduling.LRU
import com.clemaire.gexplore.core.gfa.reference.cache.Types.{C, CI, D, I}
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.{SRChunkIndex, SRIndex}

private object Types {
  type I = SRIndex
  type CI = SRChunkIndex
  type D = ReferenceNode

  type C = Chunk[CI, D]
}

case class RNCache(index: I,
                   max: Int = 25)
  extends Cache[I, CI, D]
    with SetNumberOfChunks
    with LRU[C] {

  override val reader: ChunkReader[I, CI, D] = ???

}
