package com.clemaire.gexplore.core.gfa.construction

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.GraphData
import com.clemaire.gexplore.core.gfa.reference.reading.additional.HeaderReader
import com.clemaire.gexplore.core.gfa.reference.reading.coordinates.NioBufferedGCIndexReader
import com.clemaire.gexplore.core.gfa.reference.reading.index.NioBufferedNodeIndexReader

class GraphReader(paths: CachePathList) {
  private val header = new HeaderReader(paths).read()

  private val referenceIndexReader = new NioBufferedNodeIndexReader(paths.referenceIndexPath)
  private val coordinateIndexReader = new NioBufferedGCIndexReader(paths.coordinatesIndexPath, header.genomes.size)

  def readGraphData(): GraphData =
    GraphData(paths,
      referenceIndexReader.readIndex(),
      coordinateIndexReader.readIndex(),
      header
    )
}
