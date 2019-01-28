package com.clemaire.gexplore.core.gfa.cache.reader

import com.clemaire.gexplore.core.gfa.cache.{Chunk, ChunkReader}
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait StitchedFragmentReader[I <: AbstractIndex[CI], CI <: ChunkIndex, D]
  extends ChunkReader[I, CI, D] {

  protected def readFragment(fragment: (Long, Long),
                             indices: Traversable[CI]): Map[Int, Chunk[CI, D]]

  override def readChunks(indices: Set[CI]): Map[Int, Chunk[CI, D]] = {
    val fragments = determineReadFragments(indices)

    fragments.stitched
      .flatMap(readFragment(_, indices))
      .toMap
  }

}
