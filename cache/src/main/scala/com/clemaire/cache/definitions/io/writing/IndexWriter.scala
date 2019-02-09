package com.clemaire.cache.definitions.io.writing

import java.io.Flushable

import com.clemaire.cache.definitions.index.ChunkIndex

trait IndexWriter[CI <: ChunkIndex]
  extends AutoCloseable
    with Flushable
    with DataWriter[CI] {

  /**
    * Writes a give [[ChunkIndex]] to the underlying
    * source for this [[IndexWriter]].
    *
    * @param ci The [[ChunkIndex]] to write.
    */
  def write(ci: CI): Unit

}
