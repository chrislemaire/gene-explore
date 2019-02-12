package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.order

import java.nio.file.Path

import com.clemaire.cache.impl.BasicCache
import com.clemaire.cache.impl.index.BasicIndex
import com.clemaire.gexplore.core.gfa.coordinates.data.Order
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.order.OrderReadOnlyCache.OrderReadOnlyCache

object OrderCache {
  type OrderCache = BasicCache[Order]

  def apply(dataPath: Path, indexPath: Path): OrderCache =
    new OrderCache(
      new OrderWriter(dataPath),
      new OrderReader(dataPath),
      new BasicIndex(indexPath)) {

      override def readOnly: OrderReadOnlyCache =
        new OrderReadOnlyCache(reader, index, max)
    }

}
