package com.clemaire.cache.impl.io.reading

import java.nio.file.Path

import com.clemaire.cache.definitions.index.{ChunkIndex, Index, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.reading.IndexReader
import com.clemaire.interval.IntervalTreeMap
import com.clemaire.io.reading.NioBufferedReader

abstract class NioIndexReader[CI <: ChunkIndex](val path: Path)
  extends IndexReader[CI]
    with NioBufferedReader {

  /**
    * Reads an entire [[Index]] from a source and
    * returns the completed [[Index]].
    *
    * @return The completed [[Index]] with [[ChunkIndex]]es
    *         of type [[CI]].
    */
  override def read: ReadOnlyIndex[CI] = {
    val map = new IntervalTreeMap[Integer, CI]()
    do {
      val ic = readData(this)
      map.addBinding(ic.ids, ic)
    } while (!eofReached)
    ReadOnlyIndex(map)
  }

}
