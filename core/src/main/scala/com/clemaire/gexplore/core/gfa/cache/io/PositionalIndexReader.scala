package com.clemaire.gexplore.core.gfa.cache.io

import java.nio.file.Path

import com.clemaire.cache.definitions.index.{ChunkIndex, Index}
import com.clemaire.cache.impl.io.reading.NioIndexReader
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalReadOnlyIndex}
import com.clemaire.interval.IntervalTreeMap

class PositionalIndexReader(override val path: Path)
  extends NioIndexReader[PositionalChunkIndex](path)
    with PositionalIndexDataReader {

  /**
    * Reads an entire [[Index]] from a source and
    * returns the completed [[Index]].
    *
    * @return The completed [[Index]] with [[ChunkIndex]]es
    *         of type [[PositionalChunkIndex]].
    */
  override def read: PositionalReadOnlyIndex[PositionalChunkIndex] = {
    val indexMap = new IntervalTreeMap[Integer, PositionalChunkIndex]()
    val layerMap = new IntervalTreeMap[Integer, PositionalChunkIndex]()
    do {
      val ic = readData(this)
      indexMap.addBinding(ic.ids, ic)
      layerMap.addBinding(ic.layers, ic)
    } while (!eofReached)
    PositionalReadOnlyIndex(indexMap, layerMap)
  }

}
