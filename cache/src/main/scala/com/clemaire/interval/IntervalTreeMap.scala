package com.clemaire.interval

import com.lodborg.intervaltree.{Interval, IntervalTree}

import scala.collection.mutable
import scala.collection.JavaConverters.asScalaSetConverter

class IntervalTreeMap[K <: Comparable[K], V]
  extends mutable.HashMap[Interval[K], mutable.Set[V]]
    with mutable.MultiMap[Interval[K], V] {

  /**
    * The [[IntervalTree]] used to keep track of the
    * intervals currently accumulated in this [[IntervalTreeMap]].
    */
  private[this] val intervalTree = new IntervalTree[K]()

  /**
    * Finds all [[Interval]]s containing the given point
    * and returns the values identified by these [[Interval]]s
    * wrapped in a scala set. Executes in {{{O(log(n+m))}}}
    * time as stated in [[IntervalTree]].
    *
    * @param point The point to find containing intervals of.
    * @return The set of intervals containing the point
    *         queried for.
    */
  def valuesIntersecting(point: K): Set[V] =
    query(point).flatMap(this.apply)

  /**
    * Finds all [[Interval]]s containing the given point
    * and returns the resulting set of [[Interval]]s wrapped
    * in a scala set. Executes in {{{O(log(n+m))}}} time as
    * stated in [[IntervalTree]].
    *
    * @param point The point to find containing intervals of.
    * @return The set of intervals containing the point
    *         queried for.
    */
  def query(point: K): Set[Interval[K]] =
    intervalTree.query(point).asScala.toSet

  /**
    * Finds all [[Interval]]s intersecting the given [[Interval]]
    * and returns the values identified by these [[Interval]]s
    * wrapped in a scala set. Completes in {{{O(log(n))}}} time
    * as stated in [[IntervalTree]].
    *
    * @param interval The interval to find intersecting
    *                 intervals for.
    * @return The set of [[Interval]]s intersecting the
    *         [[Interval]] queried for.
    */
  def valuesIntersecting(interval: Interval[K]): Set[V] =
    query(interval).flatMap(this.apply)

  /**
    * Finds all [[Interval]]s intersecting the given interval
    * and returns the resulting set of [[Interval]]s wrapped
    * in a scala set. Executes in {{{O(log(n+m))}}} time as
    * stated in [[IntervalTree]].
    *
    * @param interval The interval to find intersecting
    *                 intervals for.
    * @return The set of [[Interval]]s intersecting the
    *         [[Interval]] queried for.
    */
  def query(interval: Interval[K]): Set[Interval[K]] =
    intervalTree.query(interval).asScala.toSet

  override def removeBinding(key: Interval[K], value: V): IntervalTreeMap.this.type = {
    super.removeBinding(key, value)
    if (!contains(key)) intervalTree.remove(key)
    this
  }
  override def addBinding(key: Interval[K], value: V): IntervalTreeMap.this.type = {
    super.addBinding(key, value)
    if (!intervalTree.contains(key)) intervalTree.add(key)
    this
  }

}
