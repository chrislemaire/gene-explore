package com.clemaire.gexplore.core.gfa.reference.genome.cache.chunk

import com.clemaire.cache.impl.chunk.BasicChunkBuilder
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex

class GCChunkBuilder(override val max: Int = 8192)
  extends BasicChunkBuilder[GenomeCoordinate, GCChunkIndex](max)
    with GCChunkBuilderData
    with GCChunkIndexConstructor {

  /**
    * Resets the current chunk construction and increments
    * counters where necessary.
    */
  override protected def reset(): Unit = {
    super.reset()

    relativeCoordinates.clear()
    relativeCoordinates ++= currentCoordinates
  }

  /**
    * Registers a data entry to the chunk. This
    * essentially adds the data id to the range of
    * the chunk and creates a [[com.clemaire.cache.definitions.index.ChunkIndex]] of the
    * chunk if necessary.
    *
    * @param data   The data to add to the chunk.
    * @param length The length of the data on disk.
    * @return The resulting [[com.clemaire.cache.definitions.index.ChunkIndex]] or none if
    *         none was created.
    */
  override def register(data: GenomeCoordinate, length: Int): Option[GCChunkIndex] = {
    data.coordinates.foreach(c =>
      currentCoordinates.put(c._1, currentCoordinates(c._1) + c._2))

    super.register(data, length)
  }

}
