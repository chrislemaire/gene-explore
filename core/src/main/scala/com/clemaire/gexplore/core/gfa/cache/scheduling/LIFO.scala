package com.clemaire.gexplore.core.gfa.cache.scheduling

import com.clemaire.gexplore.core.gfa.cache.Chunk

import scala.collection.mutable

trait LIFO[C <: Chunk[_, _]]
  extends CacheScheduler[C] {

  private val queue: mutable.Queue[Int] = mutable.Queue()

  override protected def add(id: Int, chunk: C): Unit = {
    super.add(id, chunk)

    queue.enqueue(id)
  }

  override protected def removeNext(): Unit =
    if (queue.nonEmpty) loadedChunks.remove(queue.dequeue())

}
