package com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.reference.index.GenomeCoordinateChunkIndex
import com.clemaire.gexplore.core.gfa.DataReader
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait GCIndexDataReader
  extends DataReader[GenomeCoordinateChunkIndex] {

  /**
    * The number of genomes to read.
    */
  private[this] var _n: Int = -1

  /**
    * Sets the number of genomes included in the index
    * to be read with this reader.
    *
    * @param n Number of genomes.
    */
  protected def withNGenomes(n: Int): this.type = {
    _n = n
    this
  }

  override protected def read(os: DataInputStream)
  : GenomeCoordinateChunkIndex = GenomeCoordinateChunkIndex(
    os.readInt(),
    os.readLong(),
    os.readInt(),
    new IntegerInterval(os.readInt(), os.readInt(), Bounded.CLOSED),
    new IntegerInterval(os.readInt(), os.readInt(), Bounded.CLOSED),
    (1 to _n).map(_ => (os.readInt(), os.readLong())).toMap
  )

  override protected def read(ob: ByteBuffer)
  : GenomeCoordinateChunkIndex = GenomeCoordinateChunkIndex(
    ob.getInt(),
    ob.getLong(),
    ob.getInt(),
    new IntegerInterval(ob.getInt(), ob.getInt(), Bounded.CLOSED),
    new IntegerInterval(ob.getInt(), ob.getInt(), Bounded.CLOSED),
    (1 to _n).map(_ => (ob.getInt(), ob.getLong())).toMap
  )

}
