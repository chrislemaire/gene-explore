package com.clemaire.gexplore.core.gfa.reference.genome.cache.reading

import java.nio.file.Path

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.io.reading.NioChunkReader
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex
import com.clemaire.io.fixture.InputFixture
import metal.syntax._

class GCReader(path: Path,
               val n: Int)
  extends NioChunkReader[GenomeCoordinate, GCChunkIndex](path)
    with DataReader[GenomeCoordinate] {

  /**
    * The coordinates relative to the current chunk.
    */
  var relativeCoordinates: Map[Int, Long] = _

  /**
    * Reads the data object of type [[GenomeCoordinate]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[GenomeCoordinate]] read from
    *         the source input.
    */
  override def readData(source: InputFixture): GenomeCoordinate = {
    var k = source.getInt
    val id = source.getInt

    val coordinates = metal.mutable.HashMap[Int, Long]()
    do {
      coordinates.update(source.getInt, source.getLong)
      k -= 1
    } while (k > 0)

    GenomeCoordinate(id, coordinates.toImmutable)
  }

  /**
    * Reads a single chunk from the input source starting
    * at the file position and ending at the file position +
    * length in the given [[GCChunkIndex]].
    *
    * @param index The [[GCChunkIndex]] to read the chunk of.
    * @return The read [[Chunk]].
    */
  override def read(index: GCChunkIndex): Chunk[GenomeCoordinate] = {
    relativeCoordinates = index.relativeCoordinates
    super.read(index)
  }

}
