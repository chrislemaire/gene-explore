package com.clemaire.gexplore.core.gfa.reference.cache.chunk

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.reference.data.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.index.GCChunkIndex

case class GCChunk(index: GCChunkIndex,
                   data: Map[Int, GenomeCoordinate])
  extends Chunk[GCChunkIndex, GenomeCoordinate]
