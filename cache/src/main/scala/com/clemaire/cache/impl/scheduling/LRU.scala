package com.clemaire.cache.impl.scheduling

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.scheduling.CacheScheduler
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.index.ChunkIndex

import scala.language.postfixOps

trait LRU[D <: Identifiable, CI <: ChunkIndex]
  extends CacheScheduler[D, CI] {

  /**
    * The order in which chunks were most recently
    * requested. The first chunk in the list will
    * be removed next while the last chunk in the
    * list was most recently accessed.
    */
  private[this] var order: Set[Int] = Set.empty

  override protected[this] def add(chunk: Chunk[D]): Unit = {
    super.add(chunk)

    order = order.filter(chunk.id ==) ++ Set(chunk.id)
  }

  override protected[this] def removeNext(): Unit =
    if (order.nonEmpty) {
      loadedChunks.remove(order.head)
      order = order.dropRight(1)
    }

}
