package com.clemaire.gexplorer.core.gfa.interval

import java.util
import java.util.Comparator

import scala.collection.JavaConverters.asScalaIteratorConverter

import scala.collection.mutable

/**
  * A mapping from intervals to values of a given type.
  * The mapping can receive overlapping intervals and will
  * perceive overlaps to have all overlapping intervals'
  * values.
  *
  * @tparam V The type of value that is in this map.
  */
class IntervalMap[K <: Comparable[K], V]
  extends mutable.Map[Interval[K], V] {

  private[this] class Entry(val value: V,
                            val coveredBy: mutable.Buffer[Entry],
                            val intersects: mutable.Buffer[Entry])

  /**
    * The mapping of intervals to values sorted
    * by lower bounds.
    */
  private val lowerBoundMap = new util.TreeMap[Interval[K], Entry](
    Comparator.comparing[Interval[K], K](_.left))

  /**
    * Gets all entries with their intervals intersecting
    * the given interval.
    *
    * @param range The range to find the intersecting
    *              intervals for.
    * @return Sub-map of only entries intersecting the
    *         given range.
    */
  private[this] def getAllIntersectingEntries(range: Interval[K])
  : util.SortedMap[Interval[K], Entry] =

    if (lowerBoundMap.isEmpty) {
      new util.TreeMap()
    } else {
      val lowerKey = lowerBoundMap.floorKey(range)
      val higherKey = lowerBoundMap.higherKey(range)

      lowerBoundMap.subMap(
        if (lowerKey != null) lowerKey else lowerBoundMap.firstKey, true,
        if (higherKey != null) higherKey else lowerBoundMap.lastKey, false)
    }

  /**
    * Helps with adding a given key-value pair by splitting
    * up and merging nodes in an appropriate way.
    *
    * @param rangeToAdd The interval/key of the pair to add.
    * @param valueToAdd The value to add.
    */
  private[this] def addHelper(rangeToAdd: Interval[K],
                              valueToAdd: V): Unit = {
    val overlap = getAllIntersectingEntries(rangeToAdd)
    val entryToAdd = new Entry(valueToAdd,
      mutable.Buffer(), mutable.Buffer())

    overlap.forEach((subRange, subEntry) => {
      if (subRange.contains(rangeToAdd)) {
        entryToAdd.coveredBy += subEntry
      } else if (rangeToAdd.contains(subRange)) {
        subEntry.coveredBy += entryToAdd
      } else {
        assert(subRange.intersects(rangeToAdd))

        entryToAdd.intersects += subEntry
        subEntry.intersects += entryToAdd
      }
    })

    lowerBoundMap.put(rangeToAdd, entryToAdd)
  }

  /**
    * Adds the given key-value pair to this [[IntervalMap]].
    *
    * @param key   The key/interval of the pair to add.
    * @param value The value to add.
    */
  def add(key: Interval[K],
          value: V): Unit =
    addHelper(key, value)

  /**
    * Gets all values for which the intervals intersect
    * the given index.
    *
    * @param index Index to find intervals for.
    * @return The set of values found to intersect the
    *         given index.
    */
  def getAllAt(index: K): Set[V] =
    getAllIntersecting(index, index)

  /**
    * Gets all values for which the intervals intersect
    * the given interval.
    *
    * @param left  Left-bound of the interval to find.
    * @param right Right-bound of the interval to find.
    * @return The set of values found to intersect the
    *         given interval.
    */
  def getAllIntersecting(left: K,
                         right: K): Set[V] =
    getAllIntersecting(Interval(left, right))

  /**
    * Gets all values for which the intervals intersect
    * the given interval.
    *
    * @param range The interval to find.
    * @return The set of values found to intersect the
    *         given interval.
    */
  def getAllIntersecting(range: Interval[K]): Set[V] = {
    val result: mutable.Set[V] = mutable.HashSet()

    getAllIntersectingEntries(range).values
      .forEach(entry => result.add(entry.value))

    result.toSet
  }

  override def +=(kv: (Interval[K], V)): IntervalMap.this.type = {
    add(kv._1, kv._2)
    this
  }

  override def -=(key: Interval[K]): IntervalMap.this.type = {
    lowerBoundMap.remove(key)
    this
  }

  override def get(key: Interval[K]): Option[V] =
    Option(lowerBoundMap.get(key)).map(_.value)

  override def iterator: Iterator[(Interval[K], V)] =
    lowerBoundMap.entrySet().stream()
      .map(kv => (kv.getKey, kv.getValue.value))
      .iterator().asScala
}
