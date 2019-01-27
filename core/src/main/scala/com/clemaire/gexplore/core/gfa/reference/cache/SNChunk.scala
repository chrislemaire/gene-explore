package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.index.SRIndex

case class SNChunk(index: SRIndex,
                   nodes: Map[Int, StructuralNode]) {

  val byLayer: Map[Int, Iterable[StructuralNode]] =
    nodes.values.groupBy(_.layer)

}
