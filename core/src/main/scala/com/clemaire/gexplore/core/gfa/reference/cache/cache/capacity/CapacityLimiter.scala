package com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity

trait CapacityLimiter {

  /**
    * Decides whether this cache has reached its
    * maximum capacity and returns whether this is
    * the case.
    *
    * @return True when maximum cache capacity is reached
    *         false otherwise.
    */
  protected def reachedMax(): Boolean

}
