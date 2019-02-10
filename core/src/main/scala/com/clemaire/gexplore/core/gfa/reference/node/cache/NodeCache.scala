package com.clemaire.gexplore.core.gfa.reference.node.cache

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.cache.PositionalCache
import com.clemaire.gexplore.core.gfa.cache.index.PositionalIndex
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexWriter
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode

object NodeCache {
  type NodeCache = PositionalCache[ReferenceNode]

  def apply(dataPath: Path,
            indexPath: Path): NodeCache = new PositionalCache[ReferenceNode](
    writer = new NodeWriter(dataPath),
    reader = new NodeReader(dataPath),
    index = new PositionalIndex(new PositionalIndexWriter(indexPath)),
    max = 25
  )
}
