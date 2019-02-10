package com.clemaire.gexplore.core.gfa

import java.io.DataInputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.util.io.NioBufferedDataReader

trait NioDataReader[T]
  extends DataReader[T] {

  /**
    * Reads a single object from the given [[NioBufferedDataReader]].
    *
    * @param br The [[NioBufferedDataReader]] to read from.
    * @return The read object of type [[T]].
    */
  protected[this] def read(br: NioBufferedDataReader): T

  override protected def read(is: DataInputStream): T = ???

  override protected def read(ib: ByteBuffer): T = ???

}
