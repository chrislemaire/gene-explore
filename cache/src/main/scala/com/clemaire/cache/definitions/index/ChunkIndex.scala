package com.clemaire.cache.definitions.index

import com.clemaire.cache.definitions.Identifiable
import com.lodborg.intervaltree.IntegerInterval

trait ChunkIndex
  extends Identifiable {

  val id: Int
  val filePos: Long
  val length: Long

  val ids: IntegerInterval
}
