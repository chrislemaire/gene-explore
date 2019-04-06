package com.clemaire.gexplore.core.gfa.data

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeReadOnlyCache
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeReadOnlyCache.DummyNodeReadOnlyCache

class SecondaryGraphData(val paths: CachePathList,
                         val graphData: GraphData) {
  val dummies: DummyNodeReadOnlyCache = DummyNodeReadOnlyCache(paths.dummyPath, paths.dummyIndexPath)
}
