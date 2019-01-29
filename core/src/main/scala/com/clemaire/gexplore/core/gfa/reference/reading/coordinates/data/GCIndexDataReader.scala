package com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.reference.index.GCChunkIndex
import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.util.io.NioBufferedDataReader
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait GCIndexDataReader
  extends DataReader[GCChunkIndex] {

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

  protected def read(br: NioBufferedDataReader)
  : GCChunkIndex = GCChunkIndex(
    br.readInt,
    br.readLong,
    br.readInt,
    new IntegerInterval(br.readInt, br.readInt, Bounded.CLOSED),
    new IntegerInterval(br.readInt, br.readInt, Bounded.CLOSED),
    (1 to _n).map(_ => (br.readInt, br.readLong)).toMap
  )

  override protected def read(is: DataInputStream)
  : GCChunkIndex = GCChunkIndex(
    is.readInt(),
    is.readLong(),
    is.readInt(),
    new IntegerInterval(is.readInt(), is.readInt(), Bounded.CLOSED),
    new IntegerInterval(is.readInt(), is.readInt(), Bounded.CLOSED),
    (1 to _n).map(_ => (is.readInt(), is.readLong())).toMap
  )

  override protected def read(ib: ByteBuffer)
  : GCChunkIndex = GCChunkIndex(
    ib.getInt(),
    ib.getLong(),
    ib.getInt(),
    new IntegerInterval(ib.getInt(), ib.getInt(), Bounded.CLOSED),
    new IntegerInterval(ib.getInt(), ib.getInt(), Bounded.CLOSED),
    (1 to _n).map(_ => (ib.getInt(), ib.getLong())).toMap
  )

}
