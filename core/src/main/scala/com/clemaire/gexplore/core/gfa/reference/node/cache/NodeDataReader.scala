package com.clemaire.gexplore.core.gfa.reference.node.cache

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode
import com.clemaire.io.fixture.InputFixture

trait NodeDataReader
  extends DataReader[ReferenceNode] {

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
