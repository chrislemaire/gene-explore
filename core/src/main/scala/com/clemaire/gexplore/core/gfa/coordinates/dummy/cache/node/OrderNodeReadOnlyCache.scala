package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexReader
import com.clemaire.gexplore.core.gfa.cache.PositionalReadOnlyCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.OrderNode

object OrderNodeReadOnlyCache {
  type OrderNodeReadOnlyCache = PositionalReadOnlyCache[OrderNode, PositionalChunkIndex]


  def apply(dataPath: Path, indexPath: Path): OrderNodeReadOnlyCache =
    new OrderNodeReadOnlyCache(
      new OrderNodeReader(dataPath),
      new PositionalIndexReader(indexPath).read
    )

}
