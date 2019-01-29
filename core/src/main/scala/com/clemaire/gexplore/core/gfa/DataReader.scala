package com.clemaire.gexplore.core.gfa

import java.io.DataInputStream
import java.nio.ByteBuffer

trait DataReader[T] {

  /**
    * Reads a single object from the given [[DataInputStream]].
    *
    * @param is The [[DataInputStream]] to read from.
    * @return The read object of type [[T]].
    */
  protected[this] def read(is: DataInputStream): T

  /**
    * Reads a single object from the given [[ByteBuffer]].
    *
    * @param ib The [[ByteBuffer]] to read from.
    * @return The read object of type [[T]].
    */
  protected[this] def read(ib: ByteBuffer): T

}
