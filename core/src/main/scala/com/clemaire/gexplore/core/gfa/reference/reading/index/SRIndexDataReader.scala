package com.clemaire.gexplore.core.gfa.reference.reading.index

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.reference.index.ReferenceChunkIndex
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait SRIndexDataReader
  extends DataReader[ReferenceChunkIndex] {

  override protected def read(is: DataInputStream)
  : ReferenceChunkIndex =
    ReferenceChunkIndex(
      is.readInt(),
      is.readLong(),
      is.readInt(),
      new IntegerInterval(is.readInt, is.readInt, Bounded.CLOSED),
      new IntegerInterval(is.readInt, is.readInt, Bounded.CLOSED)
    )

  override protected def read(ib: ByteBuffer)
  : ReferenceChunkIndex =
    ReferenceChunkIndex(
      ib.getInt,
      ib.getLong,
      ib.getInt,
      new IntegerInterval(ib.getInt, ib.getInt, Bounded.CLOSED),
      new IntegerInterval(ib.getInt, ib.getInt, Bounded.CLOSED)
    )

}
