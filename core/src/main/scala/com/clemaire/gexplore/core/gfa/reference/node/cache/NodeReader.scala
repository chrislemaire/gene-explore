package com.clemaire.gexplore.core.gfa.reference.node.cache

import java.nio.file.Path

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode
import com.clemaire.io.fixture.InputFixture

class NodeReader(path: Path)
  extends NioChunkReader[ReferenceNode, PositionalChunkIndex](path)
    with DataReader[ReferenceNode] {

  /**
    * Reads the data object of type [[ReferenceNode]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[ReferenceNode]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): ReferenceNode = {
    val nOutgoing = source.getInt
    val nIncoming = source.getInt

    ReferenceNode(
      source.getInt,
      source.getInt,
      source.getLong,
      source.getInt,
      (1 to nOutgoing).map(_ => (source.getInt, source.getLong)),
      (1 to nIncoming).map(_ => (source.getInt, source.getLong))
    )
  }

}
