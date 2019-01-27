package com.clemaire.gexplore.core.gfa.reference.cache.cache.scheduling

import com.clemaire.gexplore.core.gfa.reference.cache.{Cache, Chunk}
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

import scala.collection.mutable

trait LIFO[I <: AbstractIndex[CI], D, CI <: ChunkIndex]
  extends Cache[I, D, CI] {

  private val queue: mutable.Queue[Int] = mutable.Queue()

  override protected def add(id: Int, chunk: Chunk[I, D]): Unit = {
    super.add(id, chunk)

    queue.enqueue(id)
  }

  override protected def removeNext(): Unit =
    if (queue.nonEmpty) loadedChunks.remove(queue.dequeue())

}
