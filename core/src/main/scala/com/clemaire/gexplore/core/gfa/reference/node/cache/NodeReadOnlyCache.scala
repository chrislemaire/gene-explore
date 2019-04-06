package com.clemaire.gexplore.core.gfa.reference.node.cache

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.cache.PositionalReadOnlyCache
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalIndex}
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexReader
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode

object NodeReadOnlyCache {
  type NodeReadOnlyCache = PositionalReadOnlyCache[ReferenceNode, PositionalChunkIndex]

  def apply(dataPath: Path,
            indexPath: Path): NodeReadOnlyCache = new PositionalReadOnlyCache[ReferenceNode, PositionalChunkIndex](
    reader = new NodeReader(dataPath),
    index = new PositionalIndexReader(indexPath).read,
    max = 25
  )
}
