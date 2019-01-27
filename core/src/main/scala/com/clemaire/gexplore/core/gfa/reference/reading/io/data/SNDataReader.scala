package com.clemaire.gexplore.core.gfa.reference.reading.io.data

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.DataReader
import com.clemaire.gexplore.core.gfa.reference.data.StructuralNode

trait SNDataReader
  extends DataReader[StructuralNode] {

  private def readEdge(is: DataInputStream): Int = {
    val res = is.readInt()
    is.readLong()
    res
  }

  private def readEdge(ib: ByteBuffer): Int = {
    val res = ib.getInt()
    ib.getLong
    res
  }

  override protected def read(is: DataInputStream): StructuralNode = {
    val nOutgoing = is.readInt()
    val nIncoming = is.readInt()

    StructuralNode(
      is.readInt(),
      is.readInt(),
      is.readInt(),
      (1 to nOutgoing).map(_ => readEdge(is)).toSet,
      (1 to nIncoming).map(_ => readEdge(is)).toSet
    )
  }

  override protected def read(ib: ByteBuffer): StructuralNode = {
    val nOutgoing = ib.getInt()
    val nIncoming = ib.getInt()

    StructuralNode(
      ib.getInt,
      ib.getInt,
      ib.getInt,
      (1 to nOutgoing).map(_ => readEdge(ib)).toSet,
      (1 to nIncoming).map(_ => readEdge(ib)).toSet
    )
  }

}
