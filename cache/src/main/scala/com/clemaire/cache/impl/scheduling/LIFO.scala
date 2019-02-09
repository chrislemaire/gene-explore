package com.clemaire.cache.impl.scheduling

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.scheduling.CacheScheduler
import com.clemaire.cache.definitions.Identifiable

import scala.collection.mutable

trait LIFO[D <: Identifiable, CI <: ChunkIndex]
  extends CacheScheduler[D, CI] {

  /**
    * The queue of accessed chunks. The first chunk
    * in queue will be the first to be removed as it
    * was the least recently loaded chunk.
    */
  private[this] val queue: mutable.Queue[Int] = mutable.Queue()

  override protected[this] def add(chunk: Chunk[D]): Unit = {
    super.add(chunk)

    if (!queue.contains(chunk.id))
      queue.enqueue(chunk.id)
  }

  override protected[this] def removeNext(): Unit =
    if (queue.nonEmpty) loadedChunks.remove(queue.dequeue())

}
