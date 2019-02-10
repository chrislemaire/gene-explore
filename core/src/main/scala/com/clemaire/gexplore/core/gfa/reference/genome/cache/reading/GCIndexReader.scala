package com.clemaire.gexplore.core.gfa.reference.genome.cache.reading

import java.nio.file.Path

import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioIndexReader
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex
import com.clemaire.io.fixture.InputFixture
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

class GCIndexReader(path: Path, n: Int)
  extends NioIndexReader[GCChunkIndex](path)
    with DataReader[GCChunkIndex] {

  /**
    * Reads the data object of type [[GCChunkIndex]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[GCChunkIndex]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): GCChunkIndex =
    GCChunkIndex(
      source.getInt,
      source.getLong,
      source.getInt,
      new IntegerInterval(source.getInt, source.getInt, Bounded.CLOSED),
      (1 to n).map(_ => (source.getInt, source.getLong)).toMap
    )

}
