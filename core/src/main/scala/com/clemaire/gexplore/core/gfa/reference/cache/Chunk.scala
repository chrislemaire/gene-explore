package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait Chunk[I <: ChunkIndex, D] {
  val index: I
  val data: Map[Int, D]
}
