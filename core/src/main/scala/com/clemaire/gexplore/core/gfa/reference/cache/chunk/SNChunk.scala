package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.index.SRChunkIndex

case class SNChunk(index: SRChunkIndex,
                   data: Map[Int, StructuralNode])
  extends Chunk[SRChunkIndex, StructuralNode] {

  val byLayer: Map[Int, Iterable[StructuralNode]] =
    data.values.groupBy(_.layer)

}
