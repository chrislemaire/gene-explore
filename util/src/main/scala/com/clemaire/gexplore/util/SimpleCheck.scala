package com.clemaire.gexplore.util

object SimpleCheck {

  def checkThatOrThrow(check: Boolean,
                       exception: Exception): Unit =
    if (!check) {
      throw exception
    }

}
