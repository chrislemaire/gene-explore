package com.clemaire.cache.impl.io.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.io.fixture.OutputFixture

class BasicChunkWriter[D <: Identifiable](path: Path,
                                          dataWriter: DataWriter[D])
  extends NioChunkWriter[D](path) {

  /**
    * @return The length of some object.
    */
  override def length: Int =
    dataWriter.length

  /**
    * Writes the given data object of type [[D]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: D, out: OutputFixture): Unit =
    dataWriter.writeData(data, out)

}
