package com.clemaire.gexplore.core.gfa.construction

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.GraphData
import com.clemaire.gexplore.core.gfa.reference.reading.additional.HeaderReader

class GraphReader(paths: CachePathList) {
  private val header = new HeaderReader(paths).read()

  def readGraphData(): GraphData =
    GraphData(paths, header)
}
