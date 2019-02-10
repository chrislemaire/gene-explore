package com.clemaire.gexplore.core.gfa.reference.genome.cache.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.cache.impl.io.InstanceLength
import com.clemaire.cache.impl.io.writing.NioChunkWriter
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.io.fixture.OutputFixture

class GCWriter(path: Path)
  extends NioChunkWriter[GenomeCoordinate](path)
    with DataWriter[GenomeCoordinate]
    with InstanceLength[Traversable[(Int, Long)]] {

  /**
    * Sets the object to find the length of and
    * subsequently prepares the length variable
    * with the calculated length for the object.
    *
    * @param t The object to calculate the length
    *          for.
    */
  override def forObj(t: Traversable[(Int, Long)]): Unit =
    _length = 4 + 4 + 8 * t.size

  /**
    * Writes the given data object of type [[GenomeCoordinate]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: GenomeCoordinate, out: OutputFixture): Unit = {
    out.writeInt(data.coordinates.size)
    out.writeInt(data.id)

    data.coordinates.foreach(kv => {
      out.writeInt(kv._1)
      out.writeInt(kv._2.toInt)
    })
  }

  /**
    * Writes the given data entry to the underlying
    * source.
    *
    * @param data The data to write to a source.
    */
  override def write(data: GenomeCoordinate): Unit = {
    forObj(data.coordinates)
    super.write(data)
  }

}
