package com.clemaire.cache.definitions

trait Identifiable extends Serializable {

  /**
    * The identifier of this object. An identifier
    * is an Integer unique for the object it is
    * defined for within its class of objects.
    */
  val id: Int

}
