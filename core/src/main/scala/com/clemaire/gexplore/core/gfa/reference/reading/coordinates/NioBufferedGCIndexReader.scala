package com.clemaire.gexplore.core.gfa.reference.reading.coordinates

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.reference.index.GCIndex
import com.clemaire.gexplore.core.gfa.reference.reading.IndexReader
import com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data.GCIndexDataReader
import com.clemaire.gexplore.util.io.NioBufferedDataReader

class NioBufferedGCIndexReader(path: Path,
                               n: Int)
  extends NioBufferedDataReader(path)
    with GCIndexDataReader
    with IndexReader[GCIndex] {

  private val _: Unit = {
    withNGenomes(n)
  }

  override def readIndex(): GCIndex = {
    val result = new GCIndex
    while (!eofReached)
      result += read(this)
    result
  }

}
