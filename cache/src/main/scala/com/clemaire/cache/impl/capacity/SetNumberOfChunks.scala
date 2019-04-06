package com.clemaire.cache.impl.capacity

import com.clemaire.cache.definitions.{CapacityLimiter, Identifiable, ReadOnlyCacheData}
import com.clemaire.cache.definitions.index.ChunkIndex

trait SetNumberOfChunks[D <: Identifiable, CI <: ChunkIndex]
  extends CapacityLimiter
    with ReadOnlyCacheData[D, CI] {

  /**
    * The maximum number of chunks loaded into
    * memory.
    */
  val max: Int

  /**
    * Decides whether this cache has reached its
    * maximum capacity and returns whether this is
    * the case.
    *
    * @return True when maximum cache capacity is reached
    *         false otherwise.
    */
  override protected def full: Boolean =
    loadedChunks.size >= max

}
