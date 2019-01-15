package com.clemaire.gexplore.core.gfa

import java.io.DataInputStream
import java.nio.ByteBuffer

trait DataReader[T] {

  /**
    * Reads a single object from the given [[DataInputStream]].
    *
    * @param os The [[DataInputStream]] to read from.
    * @return The read object of type T.
    */
  protected[this] def read(os: DataInputStream): T

  /**
    * Reads a single object from the given [[ByteBuffer]].
    *
    * @param ob The [[ByteBuffer]] to read from.
    * @return The read object of type T.
    */
  protected[this] def read(ob: ByteBuffer): T

}
