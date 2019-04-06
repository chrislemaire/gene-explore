package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.order

import java.nio.file.Path

import com.clemaire.cache.impl.io.reading.BasicIndexReader
import com.clemaire.cache.impl.BasicReadOnlyCache
import com.clemaire.gexplore.core.gfa.coordinates.data.Order

object OrderReadOnlyCache {
  type OrderReadOnlyCache = BasicReadOnlyCache[Order]

  def apply(dataPath: Path, indexPath: Path): OrderReadOnlyCache =
    new OrderReadOnlyCache(
      new OrderReader(dataPath),
      new BasicIndexReader(indexPath).read
    )

}
