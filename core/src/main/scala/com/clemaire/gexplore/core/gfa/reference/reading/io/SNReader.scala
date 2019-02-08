package com.clemaire.gexplore.core.gfa.reference.reading.io

import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex
import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.StructuralNode
import com.clemaire.gexplore.core.gfa.reference.cache.chunk.SNChunk
import com.clemaire.gexplore.core.gfa.reference.reading.io.data.SNDataReader

class SNReader(val paths: CachePathList)
  extends IdentifiableDataChunkReader[SNChunk, NodeChunkIndex, StructuralNode](paths.referencePath)
    with SNDataReader {

  override protected def constructChunk(index: NodeChunkIndex,
                                        data: Map[Int, StructuralNode]): SNChunk =
    SNChunk(index, data)

}
