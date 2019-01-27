package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.SRIndex

case class RNChunk(index: SRIndex,
                   nodes: Map[Int, ReferenceNode]) {

  val byLayer: Map[Int, Traversable[ReferenceNode]] =
    nodes.values.groupBy(_.layer)

}
