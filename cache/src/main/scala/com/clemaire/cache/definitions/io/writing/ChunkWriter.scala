package com.clemaire.cache.definitions.io.writing

import java.io.Flushable

import com.clemaire.cache.definitions.Identifiable

trait ChunkWriter[D <: Identifiable]
  extends AutoCloseable
    with Flushable
    with DataWriter[D] {

  /**
    * Writes the given data entry to the underlying
    * source.
    *
    * @param data The data to write to a source.
    */
  def write(data: D): Unit

}
