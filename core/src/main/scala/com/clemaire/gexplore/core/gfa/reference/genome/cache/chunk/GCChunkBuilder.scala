package com.clemaire.gexplore.core.gfa.reference.genome.cache.chunk

import com.clemaire.cache.impl.chunk.BasicChunkBuilder
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex

import scala.collection.mutable

import metal.syntax._

class GCChunkBuilder(val n: Int,
                     override val max: Int = 8192)
  extends BasicChunkBuilder[GenomeCoordinate, GCChunkIndex](max)
    with GCChunkBuilderData
    with GCChunkIndexConstructor {

  /**
    * The current total coordinates. These are
    * updated each node by adding the length of
    * the node for each of its genomes.
    */
  override protected[this] val currentCoordinates: mutable.Map[Int, Long] =
    mutable.HashMap() ++ (0 until n).map(i => i -> 0L).toMap

  /**
    * Resets the current chunk construction and increments
    * counters where necessary.
    */
  override protected def reset(): Unit = {
    super.reset()

    currentCoordinates.foreach(t =>
      relativeCoordinates.update(t._1, t._2))
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
    data.coordinates.foreach((id, v) =>
      relativeCoordinates.put(id, currentCoordinates(id) + v))

    super.register(data, length)
  }

}
