package com.clemaire.cache.definitions.index

import com.clemaire.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait ReadOnlyIndex[CI <: ChunkIndex] {

  protected[this] val index: IntervalTreeMap[Integer, CI]

  /**
    * Gets the [[ChunkIndex]]es with the given index
    * in their index ranges.
    *
    * @param id The id to lookup.
    * @return A [[Traversable]] with all [[ChunkIndex]]es
    *         with the given index in their respective range.
    */
  def get(id: Int): Traversable[CI] =
    getRange(id, id)

  /**
    * Gets the [[ChunkIndex]]es with the given index range
    * in their index ranges.
    *
    * The given range is closed and will as such include
    * both extremes.
    *
    * @param left  The left value of the range of indices to lookup.
    * @param right The right value of the range of indices to lookup.
    * @return A [[Traversable]] with all [[ChunkIndex]]es
    *         with the given index in their respective range.
    */
  def getRange(left: Int, right: Int): Traversable[CI] =
    index.valuesIntersecting(new IntegerInterval(left, right, Bounded.CLOSED))

}
