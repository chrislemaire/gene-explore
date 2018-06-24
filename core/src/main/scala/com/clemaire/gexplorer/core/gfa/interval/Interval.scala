package com.clemaire.gexplorer.core.gfa.interval

object Interval {

  def contains[T <: Comparable[T]](elem: T,
                                   left: T,
                                   right: T): Boolean =
    (left compareTo elem) <= 0 && (elem compareTo right) <= 0

}

case class Interval[T <: Comparable[T]](left: T,
                                        right: T) {

  private val _: Unit = {
    assert((left compareTo right) <= 0)
  }

  def intersectionWith(other: Interval[T]): Option[this.type] =
    intersectionWith(other.left, other.right)

  def intersectionWith(otherLeft: T,
                       otherRight: T): Option[this.type] = {
    val l: T = if ((left compareTo otherLeft) > 0) left else otherLeft
    val r: T = if ((right compareTo otherRight) < 0) right else otherRight

    if ((l compareTo r) <= 0) {
      Some(Interval(l, r))
    } else {
      None
    }
  }

  def intersects(other: Interval[T]): Boolean =
    intersects(other.left, other.right)

  def intersects(otherLeft: T,
                 otherRight: T): Boolean =
    contains(otherLeft) ||
      contains(otherRight) ||
      Interval.contains(left, otherLeft, otherRight) ||
      Interval.contains(right, otherLeft, otherRight)

  def contains(elem: T): Boolean = Interval.contains(elem, left, right)

}
