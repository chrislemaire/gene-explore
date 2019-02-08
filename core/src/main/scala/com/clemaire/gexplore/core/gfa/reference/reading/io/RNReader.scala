package com.clemaire.gexplore.core.gfa.reference.reading.io

import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.RNChunk
import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.reading.io.data.RNDataReader

class RNReader(val paths: CachePathList)
  extends IdentifiableDataChunkReader[RNChunk, NodeChunkIndex, ReferenceNode](paths.referencePath)
    with RNDataReader {

  protected def constructChunk(index: NodeChunkIndex,
                               data: Map[Int, ReferenceNode]): RNChunk =
    RNChunk(index, data)

}
