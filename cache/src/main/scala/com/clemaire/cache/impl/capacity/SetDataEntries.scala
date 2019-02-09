package com.clemaire.cache.impl.capacity

import com.clemaire.cache.definitions.{CapacityLimiter, Identifiable, ReadOnlyCacheData}
import com.clemaire.cache.definitions.index.ChunkIndex

trait SetDataEntries[D <: Identifiable, CI <: ChunkIndex]
  extends CapacityLimiter
    with ReadOnlyCacheData[D, CI] {

  /**
    * The maximum number of data entries to be
    * loaded into memory.
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
    loadedChunks.map(_._2.data.size).sum >= max

}
