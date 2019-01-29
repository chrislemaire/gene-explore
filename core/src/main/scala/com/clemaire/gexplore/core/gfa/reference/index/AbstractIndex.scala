package com.clemaire.gexplore.core.gfa.reference.index

import com.clemaire.gexplore.core.gfa.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

import scala.collection.mutable

trait ChunkIndex {
  val id: Int
  val filePos: Long
  val length: Int
  val layers: IntegerInterval
  val segmentIds: IntegerInterval
}

class AbstractIndex[T <: ChunkIndex]
  extends mutable.ArrayBuffer[T] {

  /**
    * Mapping of layer intervals to the chunks that
    * contain nodes in these layers.
    */
  private[this] val layerChunkMap =
    new IntervalTreeMap[Integer, T]()

  /**
    * Mapping of segment ID intervals to the chunks
    * that contain nodes with IDs within those intervals.
    */
  private[this] val segmentChunkMap =
    new IntervalTreeMap[Integer, T]()


  def betweenLayers(left: Int,
                    right: Int): Set[T] =
    layerChunkMap.valuesIntersecting(new IntegerInterval(left, right, Bounded.CLOSED))

  def betweenSegmentIds(leftId: Int,
                    rightId: Int): Set[T] =
    segmentChunkMap.valuesIntersecting(new IntegerInterval(leftId, rightId, Bounded.CLOSED))


  override def +=(elem: T): this.type = {
    layerChunkMap.put(elem.layers, elem)
    segmentChunkMap.put(elem.segmentIds, elem)

    super.+=(elem)
  }

  override def remove(n: Int, count: Int): Unit = {
    if (count >= 0 && n >= 0 && n + count < size0) {
      slice(n, n + count)
        .foreach(elem => {
          layerChunkMap -= (elem.layers -> elem)
          segmentChunkMap -= (elem.segmentIds -> elem)
        })
    }
    super.remove(n, count)
  }
}
