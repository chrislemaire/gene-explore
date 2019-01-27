package com.clemaire.gexplore.core.gfa.reference.cache

import scala.collection.mutable

trait CacheData[ChunkType <: Chunk[_, _]] {

  protected val loadedChunks: mutable.HashMap[Int, ChunkType] =
    mutable.HashMap()

}
