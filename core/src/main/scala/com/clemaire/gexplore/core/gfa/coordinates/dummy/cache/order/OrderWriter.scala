package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.order

import java.nio.file.Path

import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.cache.impl.io.writing.NioChunkWriter
import com.clemaire.gexplore.core.gfa.coordinates.data.Order
import com.clemaire.io.fixture.OutputFixture

class OrderWriter(path: Path)
  extends NioChunkWriter[Order](path)
    with DataWriter[Order] {

  /**
    * @return The length of some object.
    */
  override def length: Int = 4 + 4

  /**
    * Writes the given data object of type [[Order]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: Order, out: OutputFixture): Unit = {
    out.writeInt(data.id)
    out.writeInt(data.rank)
  }

}
