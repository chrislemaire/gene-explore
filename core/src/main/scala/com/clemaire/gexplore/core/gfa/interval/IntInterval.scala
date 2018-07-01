package com.clemaire.gexplore.core.gfa.interval

import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

class IntInterval(var start: Int = Int.MinValue,
                  var end: Int = Int.MaxValue) {

  def toInterval: IntegerInterval =
    new IntegerInterval(start, end, Bounded.CLOSED)

  /**
    * Updates this interval such that its range
    * contains its previous range plus the given value.
    *
    * @param value    The value to push into the interval.
    */
  def pushBoundaries(value: Int): Unit =
    if (value < start) start = value
    else if (value > end) end = value

}
