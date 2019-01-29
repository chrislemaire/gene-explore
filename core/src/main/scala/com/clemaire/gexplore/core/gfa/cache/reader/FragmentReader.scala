package com.clemaire.gexplore.core.gfa.cache.reader

import com.clemaire.gexplore.core.gfa.cache.{Chunk, ChunkReader}
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait FragmentReader[C <: Chunk[CI, _], CI <: ChunkIndex]
  extends ChunkReader[C, CI] {

  protected def readChunk(index: CI): C

  override def readChunks(indices: Set[CI]): Map[Int, C] =
    indices.map(index => index.id -> readChunk(index)).toMap

}
