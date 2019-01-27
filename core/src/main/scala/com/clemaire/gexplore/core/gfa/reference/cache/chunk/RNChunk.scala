package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.reference.cache.Chunk
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.SRChunkIndex

case class RNChunk(index: SRChunkIndex,
                   data: Map[Int, ReferenceNode])
  extends Chunk[SRChunkIndex, ReferenceNode] {

  val byLayer: Map[Int, Traversable[ReferenceNode]] =
    data.values.groupBy(_.layer)

}
