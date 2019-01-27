package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.reference.cache.Chunk
import com.clemaire.gexplore.core.gfa.reference.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.index.SRIndex

case class SNChunk(override val index: SRIndex,
                   override val data: Map[Int, StructuralNode])
  extends Chunk[SRIndex, StructuralNode] {

  val byLayer: Map[Int, Iterable[StructuralNode]] =
    data.values.groupBy(_.layer)

}
