package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.node

import java.nio.file.Path

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.coordinates.dummy.OrderNode
import com.clemaire.io.fixture.InputFixture

class OrderNodeReader(path: Path)
  extends NioChunkReader[OrderNode, PositionalChunkIndex](path)
    with DataReader[OrderNode] {

  /**
    * Reads the data object of type [[OrderNode]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[OrderNode]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): OrderNode = {
    val nOutgoing = source.getInt
    val nIncoming = source.getInt

    OrderNode(
      source.getInt,
      source.getInt,
      (1 to nOutgoing).map(_ => source.getInt).toSet,
      (1 to nIncoming).map(_ => source.getInt).toSet
    )
  }

}
