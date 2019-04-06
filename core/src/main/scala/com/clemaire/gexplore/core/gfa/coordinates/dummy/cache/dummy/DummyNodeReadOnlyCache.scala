package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.cache.PositionalReadOnlyCache
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexReader
import com.clemaire.gexplore.core.gfa.coordinates.dummy.DummyNode

object DummyNodeReadOnlyCache {
  type DummyNodeReadOnlyCache = PositionalReadOnlyCache[DummyNode, PositionalChunkIndex]

  def apply(dataPath: Path, indexPath: Path): DummyNodeReadOnlyCache =
    new DummyNodeReadOnlyCache(
      new DummyNodeReader(dataPath),
      new PositionalIndexReader(indexPath).read
    )

}
