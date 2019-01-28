package com.clemaire.gexplore.core.gfa.reference.reading.index

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.reference.index.NodeChunkIndex
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait NodeIndexDataReader
  extends DataReader[NodeChunkIndex] {

  override protected def read(is: DataInputStream)
  : NodeChunkIndex =
    NodeChunkIndex(
      is.readInt(),
      is.readLong(),
      is.readInt(),
      new IntegerInterval(is.readInt, is.readInt, Bounded.CLOSED),
      new IntegerInterval(is.readInt, is.readInt, Bounded.CLOSED)
    )

  override protected def read(ib: ByteBuffer)
  : NodeChunkIndex =
    NodeChunkIndex(
      ib.getInt,
      ib.getLong,
      ib.getInt,
      new IntegerInterval(ib.getInt, ib.getInt, Bounded.CLOSED),
      new IntegerInterval(ib.getInt, ib.getInt, Bounded.CLOSED)
    )

}