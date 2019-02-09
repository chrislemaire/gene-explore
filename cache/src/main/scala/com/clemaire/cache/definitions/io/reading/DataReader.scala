package com.clemaire.cache.definitions.io.reading

import com.clemaire.io.fixture.InputFixture

trait DataReader[D] {

  /**
    * Reads the data object of type [[D]] from an
    * [[InputFixture]].
    *
    * @param source The source represented as an
    *               [[InputFixture]].
    * @return The data object of type [[D]] read from
    *         the source input.
    */
  def readData(source: InputFixture): D

}
