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

  def readEdges(n: Int, source: InputFixture): Set[Int] =
    (1 to n).map(_ => {
      val out = source.getInt
      source.skip(8)
      out
    }).toSet

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

    val id = source.getInt
    val layer = source.getInt
    source.skip(12)

    OrderNode(
      id,
      layer,
      readEdges(nOutgoing, source),
      readEdges(nIncoming, source))
  }

}
