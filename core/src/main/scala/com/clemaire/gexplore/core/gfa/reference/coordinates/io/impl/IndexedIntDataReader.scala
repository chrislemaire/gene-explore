package com.clemaire.gexplore.core.gfa.reference.coordinates.io.impl

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.{NioDataReader, StaticLength}
import com.clemaire.gexplore.core.gfa.reference.coordinates.io.IndexedData
import com.clemaire.gexplore.util.io.NioBufferedDataReader

trait IndexedIntDataReader
  extends NioDataReader[IndexedData[Int]]
    with StaticLength {

  override protected[this] val LENGTH: Int = 4 + 4

  override protected def read(br: NioBufferedDataReader): IndexedData[Int] =
    IndexedData(br.readInt, br.readInt)

  override protected def read(is: DataInputStream): IndexedData[Int] =
    IndexedData(is.readInt(), is.readInt())

  override protected def read(ib: ByteBuffer): IndexedData[Int] =
    IndexedData(ib.getInt(), ib.getInt())

}
