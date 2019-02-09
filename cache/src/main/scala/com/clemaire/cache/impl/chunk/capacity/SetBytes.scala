package com.clemaire.cache.impl.chunk.capacity

import com.clemaire.cache.definitions.CapacityLimiter
import com.clemaire.cache.definitions.chunk.ChunkBuilderData

trait SetBytes
  extends CapacityLimiter
    with ChunkBuilderData {

  /**
    * The number of bytes a single chunk can hold at
    * maximum.
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
    dataLength >= max

}
