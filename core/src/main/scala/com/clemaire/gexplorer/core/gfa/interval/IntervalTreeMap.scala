package com.clemaire.gexplorer.core.gfa.interval

import com.lodborg.intervaltree.{Interval, IntervalTree}

import scala.collection.mutable

import scala.collection.JavaConverters.asScalaSetConverter

class IntervalTreeMap[K <: Comparable[_ >: K], V]
  extends mutable.HashMap[Interval[K], mutable.Buffer[V]] {

  private[this] val intervalTree = new IntervalTree[K]()

  def query(point: K): Set[Interval[K]] =
    intervalTree.query(point).asScala.toSet

  def query(interval: Interval[K]): Set[Interval[K]] =
    intervalTree.query(interval).asScala.toSet

  def valuesIntersecting(point: K): Set[V] =
    query(point).flatMap(this.apply)

  def valuesIntersecting(interval: Interval[K]): Set[V] =
    query(interval).flatMap(this.apply)

  def put(key: Interval[K],
          value: V): IntervalTreeMap.this.type =
    this += (key -> value)

  def +=(kv: (Interval[K], V)): IntervalTreeMap.this.type =
    this += (kv._1 -> mutable.Buffer(kv._2))

  override def +=(kv: (Interval[K], mutable.Buffer[V])): IntervalTreeMap.this.type = {
    if (!contains(kv._1)) {
      intervalTree.add(kv._1)
    }
    super.+=(kv)
  }

  override def -=(key: Interval[K]): IntervalTreeMap.this.type = {
    intervalTree.remove(key)
    super.-=(key)
  }
}
