package com.clemaire.cache.definitions.io.writing

import com.clemaire.cache.definitions.io.Length
import com.clemaire.io.fixture.OutputFixture

trait DataWriter[D]
  extends Object
    with Length {

  /**
    * Writes the given data object of type [[D]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  def writeData(data: D, out: OutputFixture): Unit

}
