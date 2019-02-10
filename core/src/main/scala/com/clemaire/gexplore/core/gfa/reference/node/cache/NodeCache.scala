package com.clemaire.gexplore.core.gfa.reference.node.cache

import java.nio.file.Path

import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.cache.impl.io.writing.NioChunkWriter
import com.clemaire.gexplore.core.gfa.cache.PositionalCache
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalIndex}
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexWriter
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode

object NodeCache {

  type NodeCache = PositionalCache[ReferenceNode]

  def apply(dataPath: Path,
            indexPath: Path): NodeCache = new PositionalCache[ReferenceNode](
    writer = new NioChunkWriter[ReferenceNode](dataPath) with NodeDataWriter,
    reader = new NioChunkReader[ReferenceNode, PositionalChunkIndex](dataPath) with NodeDataReader,
    index = new PositionalIndex(new PositionalIndexWriter(indexPath)),
    max = 25
  )

}
