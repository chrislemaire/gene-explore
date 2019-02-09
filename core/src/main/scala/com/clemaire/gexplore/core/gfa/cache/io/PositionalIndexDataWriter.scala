package com.clemaire.gexplore.core.gfa.cache.io

import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.io.fixture.OutputFixture

trait PositionalIndexDataWriter
  extends DataWriter[PositionalChunkIndex] {

  /**
    * Writes the given data object of type [[PositionalChunkIndex]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  def writeData(data: PositionalChunkIndex, out: OutputFixture): Unit = {
    out.writeInt(data.id)
    out.writeLong(data.filePosition)
    out.writeLong(data.length)
    out.writeInt(data.ids.getStart)
    out.writeInt(data.ids.getEnd)
    out.writeInt(data.layers.getStart)
    out.writeInt(data.layers.getEnd)
  }

}
