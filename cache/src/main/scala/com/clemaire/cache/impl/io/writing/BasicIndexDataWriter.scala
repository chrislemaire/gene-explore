package com.clemaire.cache.impl.io.writing

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.io.fixture.OutputFixture

trait BasicIndexDataWriter
  extends DataWriter[ChunkIndex] {

  /**
    * The constant length of some object.
    */
  override val length: Int = 4 + 8 + 8 + 4 + 4

  /**
    * Writes the given data object of type [[ChunkIndex]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: ChunkIndex, out: OutputFixture): Unit = {
    out.writeInt(data.id)
    out.writeLong(data.filePosition)
    out.writeLong(data.length)
    out.writeInt(data.ids.getStart)
    out.writeInt(data.ids.getEnd)
  }

}
