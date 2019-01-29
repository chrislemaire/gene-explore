package com.clemaire.gexplore.core.gfa.reference.reading.index

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.reference.index.NodeIndex
import com.clemaire.gexplore.core.gfa.reference.reading.IndexReader
import com.clemaire.gexplore.util.io.NioBufferedDataReader

class NioBufferedNodeIndexReader(path: Path)
  extends NioBufferedDataReader(path)
    with NodeIndexDataReader
    with IndexReader[NodeIndex] {

  override def readIndex(): NodeIndex = {
    val result = new NodeIndex
    while (!eofReached)
      result += read(this)
    result
  }

}
