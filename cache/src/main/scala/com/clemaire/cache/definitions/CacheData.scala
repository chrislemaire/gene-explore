package com.clemaire.cache.definitions

import com.clemaire.cache.definitions.index.ChunkIndex

import scala.collection.mutable

trait CacheData[D <: Identifiable, CI <: ChunkIndex]
  extends ReadOnlyCacheData[D, CI] {

  /**
    * The chunk that is currently being built represented
    * as a mapping from data entry ids to data entries.
    *
    * This mapping is needed to lookup currently unwritten
    * data entries.
    */
  protected val currentChunk: mutable.HashMap[Int, D] =
    mutable.HashMap()

}
