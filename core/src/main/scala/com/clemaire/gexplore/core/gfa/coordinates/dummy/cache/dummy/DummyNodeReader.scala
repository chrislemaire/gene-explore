package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy

import java.nio.file.Path

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.gexplore.core.gfa.coordinates.dummy.{DummyNode, SegmentDummy}
import com.clemaire.io.fixture.InputFixture

class DummyNodeReader(path: Path)
  extends NioChunkReader[DummyNode, PositionalChunkIndex](path)
    with DataReader[DummyNode] {

  /**
    * Reads the data object of type [[DummyNode]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[DummyNode]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): DummyNode =
    if (source.getInt == 0) {
      new DummyNode(source.getInt, source.getInt, source.getInt, source.getInt)
    } else {
      new SegmentDummy(source.getInt, source.getInt, source.getInt, source.getInt, source.getInt)
    }

}
