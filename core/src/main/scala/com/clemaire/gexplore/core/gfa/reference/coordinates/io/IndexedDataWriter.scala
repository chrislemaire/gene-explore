package com.clemaire.gexplore.core.gfa.reference.coordinates.io

import java.nio.file.Path

import com.clemaire.gexplore.core.gfa.{DataWriter, StaticLength}
import com.clemaire.gexplore.util.io.NioBufferedWriter

abstract class IndexedDataWriter[D](val path: Path)
  extends NioBufferedWriter
    with DataWriter[IndexedData[D]]
    with StaticLength {

  private val _: Unit = {
    withPath(path)
  }

  val indexWriter: IndexedDataIndexWriter

  def write(data: IndexedData[D],
            layer: Int): Unit = {
    checkForFlush(LENGTH)

    write(data, buffer)
    indexWriter.write(data, layer, LENGTH)
  }

  override def flush(): Unit = {
    super.flush()
    indexWriter.flush()
  }

  override def close(): Unit = {
    super.close()
    indexWriter.flush()
  }

}
