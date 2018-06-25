package com.clemaire.gexplore.core.gfa.interval

import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

class IntInterval(var start: Int = Int.MinValue,
                  var end: Int = Int.MaxValue) {

  def toInterval: IntegerInterval =
    new IntegerInterval(start, end, Bounded.CLOSED)

}
