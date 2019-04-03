package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.Identifiable
import metal.mutable.HashMap
import metal.syntax._

import scala.reflect.ClassTag

class MutableChunk[D <: Identifiable](val id: Int)(implicit D: ClassTag[D])
  extends Chunk[D] {

  var _data: HashMap[Int, D] = HashMap.empty[Int, D]

  def data: metal.immutable.HashMap[Int, D] = _data.toImmutable

  def +=(d: D): Unit = _data.update(d.id, d)

}
