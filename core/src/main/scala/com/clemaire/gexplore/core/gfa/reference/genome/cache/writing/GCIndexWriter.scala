package com.clemaire.gexplore.core.gfa.reference.genome.cache.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.cache.impl.io.writing.NioIndexWriter
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex
import com.clemaire.io.fixture.OutputFixture

class GCIndexWriter(path: Path, n: Int)
  extends NioIndexWriter[GCChunkIndex](path)
    with DataWriter[GCChunkIndex] {

  /**
    * @return The length of some object.
    */
  override val length: Int = 4 + 8 + 8 + 4 + 4 + 8 * n

  /**
    * Writes the given data object of type [[GCChunkIndex]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: GCChunkIndex, out: OutputFixture): Unit = {
    out.writeInt(data.id)
    out.writeLong(data.filePosition)
    out.writeLong(data.length)
    out.writeInt(data.ids.getStart)
    out.writeInt(data.ids.getEnd)

    (0 until n).foreach(i => {
      out.writeLong(data.relativeCoordinates(i))
    })
  }

}
