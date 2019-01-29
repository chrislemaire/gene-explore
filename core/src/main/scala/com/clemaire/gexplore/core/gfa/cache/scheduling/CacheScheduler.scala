package com.clemaire.gexplore.core.gfa.cache.scheduling

import com.clemaire.gexplore.core.gfa.cache.{CacheData, Chunk}

trait CacheScheduler[C <: Chunk[_, _]]
  extends Object
    with CacheData[C] {

  /**
    * Adds a chunk to the loaded chunks map and performs
    * any necessary cache scheduling activities.
    *
    * @param id    The index of the chunk to add.
    * @param chunk The chunk to add itself.
    */
  protected def add(id: Int,
                    chunk: C): Unit =
    loadedChunks(id) = chunk

  /**
    * Decides the next chunk to unload and removes it
    * from the loadedChunk map.
    */
  protected def removeNext(): Unit

}
