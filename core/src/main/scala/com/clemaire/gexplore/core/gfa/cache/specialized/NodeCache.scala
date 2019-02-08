package com.clemaire.gexplore.core.gfa.cache.specialized

import com.clemaire.gexplore.core.gfa.cache.Chunk
import com.clemaire.gexplore.core.gfa.data.{Identifiable, Positional}
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

abstract class NodeCache[C <: Chunk[CI, D], CI <: ChunkIndex, D <: Identifiable with Positional]
  extends IndexedCache[C, CI, D]
    with PositionalCache[C, CI, D]
