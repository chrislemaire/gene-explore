package com.clemaire.gexplore.core.gfa.coordinates.dummy

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.coordinates.dummy.algorithm.DummyConstructor
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeCache.DummyNodeCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeReadOnlyCache.DummyNodeReadOnlyCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node.OrderNodeReadOnlyCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node.OrderNodeReadOnlyCache.OrderNodeReadOnlyCache
import com.clemaire.gexplore.core.gfa.data.GraphHeader

class DummyCacheConstructor(paths: CachePathList,
                            header: GraphHeader) {

  private val orderNodes: OrderNodeReadOnlyCache = OrderNodeReadOnlyCache(paths.referencePath, paths.referenceIndexPath)

  private val dummies: DummyNodeCache = DummyNodeCache(paths.dummyPath, paths.dummyIndexPath)

  def construct: DummyNodeReadOnlyCache = {
    val dc = new DummyConstructor(orderNodes, orderNodes.maxId)

    val layerRange = 1000

    (0 until orderNodes.maxLayer by layerRange).foreach(layer => {
      val curr = orderNodes.getLayerRange(layer, layer + layerRange)
      dummies ++= dc.construct(curr)
    })

    dummies.finish
    dummies.readOnly
  }

}
