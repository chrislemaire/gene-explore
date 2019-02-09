package com.clemaire.cache.definitions.io.reading

import com.clemaire.cache.definitions.index.{ChunkIndex, Index, ReadOnlyIndex}

trait IndexReader[CI <: ChunkIndex]
  extends AutoCloseable
    with DataReader[CI] {

  /**
    * Reads an entire [[Index]] from a source and
    * returns the completed [[Index]].
    *
    * @return The completed [[Index]] with [[ChunkIndex]]es
    *         of type [[CI]].
    */
  def read: ReadOnlyIndex[CI]

}
