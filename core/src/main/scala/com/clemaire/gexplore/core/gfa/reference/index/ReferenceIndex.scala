package com.clemaire.gexplore.core.gfa.reference.index

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.interval.IntervalTreeMap
import com.lodborg.intervaltree.IntegerInterval

import scala.collection.mutable

case class ReferenceChunkIndex private[index](id: Int,
                                              length: Int,
                                              filePos: Long,
                                              layers: IntegerInterval,
                                              segmentIds: IntegerInterval)

class ReferenceIndex(val paths: CachePathList)
  extends mutable.ArrayBuffer[ReferenceChunkIndex] {

  /**
    * Mapping of layer intervals to the chunks that
    * contain nodes in these layers.
    */
  private[this] val layerChunkMap =
    new IntervalTreeMap[Integer, ReferenceChunkIndex]()

  /**
    * Mapping of segment ID intervals to the chunks
    * that contain nodes with IDs within those intervals.
    */
  private[this] val segmentChunkMap =
    new IntervalTreeMap[Integer, ReferenceChunkIndex]()

  override def +=:(elem: ReferenceChunkIndex): ReferenceIndex.this.type = {
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
