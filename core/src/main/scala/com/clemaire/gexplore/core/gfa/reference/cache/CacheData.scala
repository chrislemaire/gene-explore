package com.clemaire.gexplore.core.gfa.reference.cache

import scala.collection.mutable

trait CacheData[C <: Chunk[_, _]] {

  protected val loadedChunks: mutable.HashMap[Int, C] =
    mutable.HashMap()

}
