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
