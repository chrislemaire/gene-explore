package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy

import com.clemaire.gexplore.core.gfa.cache.chunk.{PositionalChunkBuilder, PositionalChunkIndexConstructor}
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.coordinates.dummy.DummyNode

class DummyNodeChunkBuilder
  extends PositionalChunkBuilder[DummyNode, PositionalChunkIndex]
    with PositionalChunkIndexConstructor {

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
  override def register(data: DummyNode, length: Int): Option[PositionalChunkIndex] = {
    layers.pushBoundaries(data.layerStart)
    layers.pushBoundaries(data.layerEnd)

    super.register(data, length)
  }

}
