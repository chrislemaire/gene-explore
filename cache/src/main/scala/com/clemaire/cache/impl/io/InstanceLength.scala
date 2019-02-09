package com.clemaire.cache.impl.io

import com.clemaire.cache.definitions.io.Length

trait InstanceLength[T]
  extends Length {

  /**
    * Sets the object to find the length of and
    * subsequently prepares the length variable
    * with the calculated length for the object.
    *
    * @param t The object to calculate the length
    *          for.
    */
  def forObj(t: T): Unit

  /**
    * The length of the given object of type [[T]].
    */
  protected[this] var _length: Int = 0

  override def length: Int = _length

}
