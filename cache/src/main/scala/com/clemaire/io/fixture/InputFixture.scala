package com.clemaire.io.fixture

trait InputFixture {

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
