package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.Identifiable

trait Chunk[D <: Identifiable]
  extends Identifiable {

  /**
    * The data held in this chunk mapped by
    * its individual ids.
    */
  val data: Map[Int, D]

}

object Chunk {
  private case class BasicChunk[D <: Identifiable]
  (id: Int,
   data: Map[Int, D]) extends Chunk[D]

  def apply[D <: Identifiable](id: Int,
                               data: Map[Int, D]): Chunk[D] =
    BasicChunk(id, data)
}
