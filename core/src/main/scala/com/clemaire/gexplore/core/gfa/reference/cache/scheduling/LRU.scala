package com.clemaire.gexplore.core.gfa.reference.cache.scheduling

import com.clemaire.gexplore.core.gfa.reference.cache.Chunk

import scala.collection.mutable

trait LRU[C <: Chunk[_, _]]
  extends CacheScheduler[C] {

  private var currInstance: Long = -1L

  private val order: mutable.TreeMap[Long, Int] = mutable.TreeMap()

  override protected def add(id: Int, chunk: C): Unit = {
    super.add(id, chunk)

    currInstance += 1
    if (order.contains(id))
      order.remove(id)
    order(currInstance) = id
  }

  override protected def removeNext(): Unit =
    if (order.nonEmpty) {
      val min = order.min
      loadedChunks.remove(min._2)
      order.remove(min._1)
    }

}
