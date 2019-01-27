package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.index.SRIndex

case class SNChunk(index: SRIndex,
                   segments: Map[Int, StructuralNode]) {

  val byLayer: Map[Int, Iterable[StructuralNode]] =
    segments.values.groupBy(_.layer)

}
