package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.SRIndex

case class RNChunk(override val index: SRIndex,
                   override val data: Map[Int, ReferenceNode])
  extends Chunk[SRIndex, ReferenceNode] {

  val byLayer: Map[Int, Traversable[ReferenceNode]] =
    data.values.groupBy(_.layer)

}
