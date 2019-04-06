package com.clemaire.cache.definitions.scheduling

import com.clemaire.cache.definitions.{Identifiable, ReadOnlyCacheData}
import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.index.ChunkIndex

trait CacheScheduler[D <: Identifiable, CI <: ChunkIndex]
  extends Object
    with ReadOnlyCacheData[D, CI] {

  /**
    * Adds a chunk to the loaded chunks map and performs
    * any necessary cache scheduling activities.
    *
    * @param chunk The chunk to add itself.
    */
  protected[this] def add(chunk: Chunk[D]): Unit =
    loadedChunks(chunk.id) = chunk

  /**
    * Decides the next chunk to unload and removes it
    * from the loadedChunk map.
    */
  protected[this] def removeNext(): Unit

}
