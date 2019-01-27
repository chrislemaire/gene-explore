package com.clemaire.gexplore.core.gfa.reference.cache.cache.scheduling

import com.clemaire.gexplore.core.gfa.reference.cache.{Cache, Chunk}
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

import scala.collection.mutable

trait LRU[I <: AbstractIndex[CI], D, CI <: ChunkIndex]
  extends Cache[I, D, CI] {

  private var currInstance: Long = -1L

  private val order: mutable.TreeMap[Long, Int] = mutable.TreeMap()

  override protected def add(id: Int, chunk: Chunk[I, D]): Unit = {
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
