package com.clemaire.io.fixture

trait OutputFixture {

  /**
    * Writes a byte to the underlying source.
    *
    * @param v The byte to write.
    */
  def writeByte(v: Byte): Unit

  /**
    * Writes a character to the underlying source.
    *
    * @param v The Char to write.
    */
  def writeChar(v: Char): Unit

  /**
    * Writes a short to the underlying source.
    *
    * @param v The Short to write.
    */
  def writeShort(v: Short): Unit

  /**
    * Writes an integer to the underlying source.
    *
    * @param v The Int to write.
    */
  def writeInt(v: Int): Unit

  /**
    * Writes a long to the underlying source.
    *
    * @param v The Long to write.
    */
  def writeLong(v: Long): Unit

}
