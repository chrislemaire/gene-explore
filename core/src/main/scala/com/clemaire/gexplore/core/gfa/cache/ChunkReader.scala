package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

case class ReadFragments(original: List[(Long, Long)],
                         stitched: List[(Long, Long)])

trait ChunkReader[I <: AbstractIndex[CI], CI <: ChunkIndex, D]
  extends AutoCloseable {

  protected def determineReadFragments(indices: Set[CI]): ReadFragments = {
    val fragments = indices.map(ic => (ic.filePos, ic.filePos + ic.length.toLong))
      .toList.sortBy(_._1)

    val stitchedFragments = fragments.indices.take(fragments.size - 1)
      .filter(i => fragments(i)._2 == fragments(i + 1)._1)
      .map(fragments)
      .toList

    ReadFragments(fragments, stitchedFragments)
  }

  def readChunks(indices: Set[CI]): Map[Int, Chunk[CI, D]]

}
