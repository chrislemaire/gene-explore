package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.cache.definitions.ReadOnlyCache
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalReadOnlyIndex}
import com.clemaire.gexplore.core.gfa.data.{Identifiable, Positional}

class PositionalReadOnlyCache[D <: Identifiable with Positional, PCI <: PositionalChunkIndex]
(val reader: ChunkReader[D, PCI],
 val index: PositionalReadOnlyIndex[PCI],
 val max: Int = 25)
  extends ReadOnlyCache[D, PCI]
    with SetNumberOfChunks[D, ChunkIndex]
    with LRU[D, ChunkIndex] {

  /**
    * Gets a data entry by its layer from the underlying
    * Cache implementation.
    *
    * @param layer The layer to lookup a data entry for.
    * @return An [[Option]] containing the data entry
    *         if one exists with the given layer.
    */
  def getLayer(layer: Int): Option[D] =
    getLayerRange(layer, layer).find(_._2.layer == layer).map(_._2)

  /**
    * Gets a range of data entries by their layers from
    * the underlying Cache implementation.
    *
    * The given range is closed and as such will include
    * both extremes.
    *
    * @param left  The left-bound for the range of data
    *              entry layers to fetch.
    * @param right The right-bound for the range of data
    *              entry layers to fetch.
    * @return A [[Map]] containing all existing data entries
    *         mapped by their id with a layer in the given range.
    */
  def getLayerRange(left: Int, right: Int): Map[Int, D] =
    getChunksByIndexes(index.getLayerRange(left, right))
      .flatMap(_.data)
      .filter(data => left <= data._1 && data._1 <= right)
      .toMap

}
