package com.clemaire.gexplorer.core.gfa.interval

import com.lodborg.intervaltree.Interval
import com.lodborg.intervaltree.Interval.Bounded

class IntInterval(var start: Int = Int.MinValue,
                  var end: Int = Int.MaxValue)
  extends Interval[Int](start, end, Bounded.CLOSED) {

  def toInterval: Interval[Int] =
    new IntInterval(start, end)

  override def getStart: Int = start

  override def getEnd: Int = end

  override def create(): Interval[Int] =
    new IntInterval()

  override def getMidpoint: Int = {
    if (isEmpty) return null
    (getStart + getEnd) / 2
  }

}
