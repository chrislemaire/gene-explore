package com.clemaire.cache.impl.io.reading

import java.nio.file.Path

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.io.fixture.InputFixture

class BasicChunkReader[D <: Identifiable, CI <: ChunkIndex](path: Path,
                                                            dataReader: DataReader[D])
  extends NioChunkReader[D, CI](path) {

  /**
    * Reads the data object of type [[D]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[D]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): D =
    dataReader.readData(source)

}
