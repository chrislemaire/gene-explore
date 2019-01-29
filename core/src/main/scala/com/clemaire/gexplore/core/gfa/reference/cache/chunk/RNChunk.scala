package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex

case class RNChunk(index: NodeChunkIndex,
                   data: Map[Int, ReferenceNode])
  extends Chunk[NodeChunkIndex, ReferenceNode] {

  val byLayer: Map[Int, Traversable[ReferenceNode]] =
    data.values.groupBy(_.layer)

}
