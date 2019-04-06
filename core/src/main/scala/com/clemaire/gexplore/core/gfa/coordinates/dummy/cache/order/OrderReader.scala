package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.order

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.gexplore.core.gfa.coordinates.data.Order
import com.clemaire.io.fixture.InputFixture

class OrderReader(path: Path)
  extends NioChunkReader[Order, ChunkIndex](path)
    with DataReader[Order] {

  /**
    * Reads the data object of type [[Order]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[Order]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): Order =
    Order(source.getInt, source.getInt)

}
