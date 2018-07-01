package com.clemaire.gexplore.core.gfa

import java.io.DataOutputStream
import java.nio.ByteBuffer

trait DataWriter[T] {

  /**
    * Writes a single object to the given [[DataOutputStream]].
    *
    * @param obj The object of type T to write.
    * @param os   The [[DataOutputStream]] to write to.
    */
  protected[this] def write(obj: T, os: DataOutputStream): Unit

  /**
    * Writes a single object to the given [[ByteBuffer]].
    *
    * @param obj The object of type T to write.
    * @param ob   The [[ByteBuffer]] to write to.
    */
  protected[this] def write(obj: T, ob: ByteBuffer): Unit

}
