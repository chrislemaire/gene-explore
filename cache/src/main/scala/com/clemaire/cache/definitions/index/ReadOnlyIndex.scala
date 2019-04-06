package com.clemaire.cache.definitions.index

import com.clemaire.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait ReadOnlyIndex[CI <: ChunkIndex] {

  /**
    * The tree-map used to lookup ids.
    */
  protected[this] val index: IntervalTreeMap[Integer, CI]

  /**
    * @return The highest id in the index chunks.
    */
  def maxId: Int =
    index.flatMap(_._2.map(_.ids.getEnd)).max

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
    * Gets the [[ChunkIndex]]es that have one of the
    * given ids in their index ranges.
    *
    * @param ids The ids to lookup.
    * @return A [[Traversable]] with all [[ChunkIndex]]es
    *         with one of the given ids in their respective range.
    */
  def get(ids: Traversable[Int]): Traversable[CI] =
    ids.flatMap(get)

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
    *         with the given range in their respective id range.
    */
  def getRange(left: Int, right: Int): Traversable[CI] =
    index.valuesIntersecting(new IntegerInterval(left, right, Bounded.CLOSED))

}

object ReadOnlyIndex {
  private case class BasicReadOnlyIndex[CI <: ChunkIndex]
  (index: IntervalTreeMap[Integer, CI]) extends ReadOnlyIndex[CI]

  /**
    * Constructs a new [[ReadOnlyIndex]] from the given
    * index.
    *
    * @param index The index to build a [[ReadOnlyIndex]]
    *              from.
    * @tparam CI The type of [[ChunkIndex]] to use in this
    *            read-only index.
    * @return The constructed [[ReadOnlyIndex]].
    */
  def apply[CI <: ChunkIndex](index: IntervalTreeMap[Integer, CI]): ReadOnlyIndex[CI] =
    BasicReadOnlyIndex(index)
}
