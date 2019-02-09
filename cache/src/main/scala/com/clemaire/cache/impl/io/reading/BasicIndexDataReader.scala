package com.clemaire.cache.impl.io.reading

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.index.BasicChunkIndex
import com.clemaire.io.fixture.InputFixture
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait BasicIndexDataReader
  extends DataReader[ChunkIndex] {

  /**
    * Reads the data object of type [[ChunkIndex]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[ChunkIndex]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): ChunkIndex =
    BasicChunkIndex(
      source.getInt,
      source.getLong,
      source.getLong,
      new IntegerInterval(source.getInt, source.getInt, Bounded.CLOSED)
    )

}
