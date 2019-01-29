package com.clemaire.gexplore.core.gfa.cache.reader

import com.clemaire.gexplore.core.gfa.cache.{Chunk, ChunkReader}
import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait StitchedFragmentReader[C <: Chunk[CI, _], CI <: ChunkIndex]
  extends ChunkReader[C, CI] {

  protected def readFragment(fragment: (Long, Long),
                             indices: Traversable[CI]): Map[Int, C]

  override def readChunks(indices: Set[CI]): Map[Int, C] = {
    val fragments = determineReadFragments(indices)

    fragments.stitched
      .flatMap(readFragment(_, indices))
      .toMap
  }

}
