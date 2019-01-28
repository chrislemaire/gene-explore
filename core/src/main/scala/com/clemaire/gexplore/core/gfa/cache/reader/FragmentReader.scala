package com.clemaire.gexplore.core.gfa.cache.reader

import com.clemaire.gexplore.core.gfa.cache.{Chunk, ChunkReader}
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait FragmentReader[I <: AbstractIndex[CI], CI <: ChunkIndex, D]
  extends ChunkReader[I, CI, D] {

  protected def readChunk(index: CI): Chunk[CI, D]

  override def readChunks(indices: Set[CI]): Map[Int, Chunk[CI, D]] =
    indices.map(index => index.id -> readChunk(index)).toMap

}
