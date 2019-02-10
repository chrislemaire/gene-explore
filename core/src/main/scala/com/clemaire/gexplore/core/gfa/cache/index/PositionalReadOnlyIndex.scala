package com.clemaire.gexplore.core.gfa.cache.index

import com.clemaire.cache.definitions.index.{ChunkIndex, ReadOnlyIndex}
import com.clemaire.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait PositionalReadOnlyIndex[PCI <: PositionalChunkIndex]
  extends ReadOnlyIndex[PCI] {

  /**
    * The interval index mapping layer ranges to their
    * respective [[PositionalChunkIndex]]es.
    */
  val layerIndex: IntervalTreeMap[Integer, PCI]

  /**
    * Gets the [[ChunkIndex]]es with the given layer
    * in their layer ranges.
    *
    * @param layer The layer to lookup.
    * @return A [[Traversable]] with all [[ChunkIndex]]es
    *         with the given range in their respective layer range.
    */
  def getLayer(layer: Int): Traversable[PCI] =
    getLayerRange(layer, layer)

  /**
    * Gets the [[ChunkIndex]]es with the given layer range
    * in their layer ranges.
    *
    * The given range is closed and will as such include
    * both extremes.
    *
    * @param left  The left value of the range of layers to lookup.
    * @param right The right value of the range of layers to lookup.
    * @return A [[Traversable]] with all [[ChunkIndex]]es
    *         with the given range in their respective layer range.
    */
  def getLayerRange(left: Int, right: Int): Traversable[PCI] =
    layerIndex.valuesIntersecting(new IntegerInterval(left, right, Bounded.CLOSED))

}

object PositionalReadOnlyIndex {
  private case class BasicPositionalReadOnlyIndex[PCI <: PositionalChunkIndex]
  (index: IntervalTreeMap[Integer, PCI],
   layerIndex: IntervalTreeMap[Integer, PCI])
    extends PositionalReadOnlyIndex[PCI]

  /**
    * Constructs a new [[ReadOnlyIndex]] from the given
    * index.
    *
    * @param index The index to build a [[ReadOnlyIndex]]
    *              from.
    * @tparam PCI The type of [[PositionalChunkIndex]] to use
    *             in this read-only index.
    * @return The constructed [[ReadOnlyIndex]].
    */
  def apply[PCI <: PositionalChunkIndex](index: PositionalIndex[PCI]): PositionalReadOnlyIndex[PCI] =
    BasicPositionalReadOnlyIndex(index, index.layerIndex)

}
