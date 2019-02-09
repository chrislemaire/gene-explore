package com.clemaire.cache.impl.index

import com.clemaire.cache.definitions.index.{ChunkIndex, ReadOnlyIndex}
import com.clemaire.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

class BasicReadOnlyIndex[CI <: ChunkIndex](val index: IntervalTreeMap[Integer, CI])
  extends ReadOnlyIndex[CI] {

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
  override def getRange(left: Int, right: Int): Traversable[CI] =
    index.valuesIntersecting(new IntegerInterval(left, right, Bounded.CLOSED))

}
