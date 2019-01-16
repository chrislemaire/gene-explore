package com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.DataReader

trait GCDataReader
  extends DataReader[(Int, Map[Int, Int])] {

  override protected def read(is: DataInputStream)
  : (Int, Map[Int, Int]) = {
    val k = is.readInt()
    (is.readInt(),
      (1 to k).map(_ => (is.readInt(), is.readInt())).toMap)
  }

  override protected def read(ib: ByteBuffer)
  : (Int, Map[Int, Int]) = {
    val k = ib.getInt()
    (ib.getInt(),
      (1 to k).map(_ => (ib.getInt(), ib.getInt())).toMap)
  }
}
