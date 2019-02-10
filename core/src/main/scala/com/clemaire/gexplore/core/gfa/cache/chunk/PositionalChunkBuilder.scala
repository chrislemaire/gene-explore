package com.clemaire.gexplore.core.gfa.cache.chunk

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.impl.chunk.BasicChunkBuilder
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.Positional
import com.clemaire.interval.IntInterval

abstract class PositionalChunkBuilder[D <: Identifiable with Positional, PCI <: PositionalChunkIndex]
(override val max: Int = 1024 * 1024)
  extends BasicChunkBuilder[D, PCI](max)
    with PositionalChunkBuilderData {

  /**
    * Resets the current chunk construction and increments
    * counters where necessary.
    */
  override protected[this] def reset(): Unit = {
    super.reset()

    layers = new IntInterval(Int.MaxValue, Int.MinValue)
  }

  /**
    * Registers a data entry to the chunk. This
    * essentially adds the data id to the range of
    * the chunk and creates a [[PositionalChunkIndex]] of the
    * chunk if necessary.
    *
    * @param data   The data to add to the chunk.
    * @param length The length of the data on disk.
    * @return The resulting [[PositionalChunkIndex]] or none if
    *         none was created.
    */
  override def register(data: D, length: Int): Option[PCI] = {
    layers.pushBoundaries(data.layer)

    super.register(data, length)
  }

}
