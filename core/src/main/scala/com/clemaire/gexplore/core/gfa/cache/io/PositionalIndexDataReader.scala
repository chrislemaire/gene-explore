package com.clemaire.gexplore.core.gfa.cache.io

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex
import com.clemaire.io.fixture.InputFixture
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait PositionalIndexDataReader
  extends DataReader[PositionalChunkIndex] {

  /**
    * Reads the data object of type [[PositionalChunkIndex]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[PositionalChunkIndex]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): PositionalChunkIndex =
    PositionalChunkIndex(
      source.getInt,
      source.getLong,
      source.getLong,
      new IntegerInterval(source.getInt, source.getInt, Bounded.CLOSED),
      new IntegerInterval(source.getInt, source.getInt, Bounded.CLOSED)
    )

}
