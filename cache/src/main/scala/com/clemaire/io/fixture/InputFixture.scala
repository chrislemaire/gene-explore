package com.clemaire.io.fixture

trait InputFixture {

  /**
    * Skips the given  number of bytes in the input.
    *
    * @param nBytes The number of bytes to skip.
    */
  def skip(nBytes: Int): Unit

  /**
    * Reads a byte from the underlying source
    * and returns it.
    *
    * @return The read byte.
    */
  def getByte: Byte

  /**
    * Reads a character from the underlying source
    * and returns it.
    *
    * @return The read Char.
    */
  def getChar: Char

  /**
    * Reads a short from the underlying source
    * and returns it.
    *
    * @return The read Short.
    */
  def getShort: Short

  /**
    * Reads an integer from the underlying source
    * and returns it.
    *
    * @return The read Int.
    */
  def getInt: Int

  /**
    * Reads a long from the underlying source
    * and returns it.
    *
    * @return The read Long.
    */
  def getLong: Long

}
