package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex

case class SNChunk(index: NodeChunkIndex,
                   data: Map[Int, StructuralNode])
  extends Chunk[NodeChunkIndex, StructuralNode] {

  val byLayer: Map[Int, Traversable[StructuralNode]] =
    data.values.groupBy(_.layer)

}
