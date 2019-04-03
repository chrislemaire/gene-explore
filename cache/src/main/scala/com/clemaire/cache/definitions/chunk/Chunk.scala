package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.Identifiable
import metal.immutable.HashMap

import scala.reflect.ClassTag

trait Chunk[D <: Identifiable]
  extends Identifiable {

  /**
    * The data held in this chunk mapped by
    * its individual ids.
    */
  def data: HashMap[Int, D]

}

object Chunk {
  private case class BasicChunk[D <: Identifiable]
  (id: Int,
   data: HashMap[Int, D]) extends Chunk[D]

  def apply[D <: Identifiable](id: Int,
                               data: HashMap[Int, D])
                              (implicit D: ClassTag[D]): Chunk[D] =
    BasicChunk(id, data)
}
