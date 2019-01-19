package com.clemaire.gexplore.core.gfa.reference.reading.io

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.data.StructuralNode

trait SRDataReader
  extends DataReader[StructuralNode] {

  override protected def read(is: DataInputStream): StructuralNode = {
    val nOutgoing = is.readInt()
    val nIncoming = is.readInt()

    StructuralNode(
      is.readInt(),
      is.readInt(),
      is.readLong(),
      is.readInt(),
      (1 to nOutgoing).map(_ => (is.readInt(), is.readLong())),
      (1 to nIncoming).map(_ => (is.readInt(), is.readLong()))
    )
  }

  override protected def read(ib: ByteBuffer): StructuralNode = {
    val nOutgoing = ib.getInt()
    val nIncoming = ib.getInt()

    StructuralNode(
      ib.getInt,
      ib.getInt,
      ib.getLong,
      ib.getInt,
      (1 to nOutgoing).map(_ => (ib.getInt, ib.getLong)),
      (1 to nIncoming).map(_ => (ib.getInt, ib.getLong))
    )
  }

}
