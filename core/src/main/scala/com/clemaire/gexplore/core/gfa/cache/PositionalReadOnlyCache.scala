package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.cache.definitions.{Identifiable, ReadOnlyCache}
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.scheduling.LRU
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalReadOnlyIndex}
import com.clemaire.gexplore.core.gfa.Positional
import metal.mutable.HashMap
import metal.syntax._

import scala.reflect.ClassTag

class PositionalReadOnlyCache[D <: Identifiable with Positional, PCI <: PositionalChunkIndex]
(val reader: ChunkReader[D, PCI],
 val index: PositionalReadOnlyIndex[PCI],
 val max: Int = 25)
(override protected implicit val D: ClassTag[D])
  extends ReadOnlyCache[D, PCI]
    with SetNumberOfChunks[D, PCI]
    with LRU[D, PCI] {

  /**
    * @return The highest layer in this Cache.
    */
  def maxLayer: Int = index.maxLayer

  /**
    * Gets a data entry by its layer from the underlying
    * Cache implementation.
    *
    * @param layer The layer to lookup a data entry for.
    * @return An [[Option]] containing the data entry
    *         if one exists with the given layer.
    */
  def getLayer(layer: Int): HashMap[Int, D] =
    getLayerRange(layer, layer).getOrElse(layer, HashMap.empty)

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
  def getLayerRange(left: Int, right: Int): HashMap[Int, HashMap[Int, D]] = {
    val chunks = getChunksByIndexes(index.getLayerRange(left, right))
    val result = HashMap[Int, HashMap[Int, D]]()

    chunks.foreach(c => {
      c.data.foreach((id, d) => {
        if (left <= d.layer && d.layer <= right)
          if (result.contains(d.layer))
            result(d.layer).update(id, d)
          else
            result(d.layer) = HashMap(id -> d)
      })
    })

    result
  }

}
