package com.clemaire.gexplore.core.gfa

trait LengthByType[T] {

  /**
    * Calculates the length of a given object of type T
    * when written to disk in bytes.
    *
    * @param obj The object of type T to calculate the
    *             length of.
    * @return The length of a written version of the given
    *         object in bytes.
    */
  protected[this] def length(obj: T): Int

}
