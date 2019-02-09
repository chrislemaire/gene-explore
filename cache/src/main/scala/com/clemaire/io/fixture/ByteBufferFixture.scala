package com.clemaire.io.fixture

import java.nio.ByteBuffer

import scala.language.implicitConversions

object ByteBufferFixture {
  implicit def bb2fixtureOut(bb: ByteBuffer): OutputFixture =
    new ByteBufferOutputFixture(bb)

  implicit def bb2fixtureIn(bb: ByteBuffer): InputFixture =
    new ByteBufferInputFixture(bb)
}

class ByteBufferOutputFixture(bb: ByteBuffer)
  extends OutputFixture {

  override def writeByte(v: Byte): Unit = bb.put(v)
  override def writeChar(v: Char): Unit = bb.putChar(v)
  override def writeShort(v: Short): Unit = bb.putShort(v)
  override def writeInt(v: Int): Unit = bb.putInt(v)
  override def writeLong(v: Long): Unit = bb.putLong(v)
}

class ByteBufferInputFixture(bb: ByteBuffer)
  extends InputFixture {

  override def getByte: Byte = bb.get()
  override def getChar: Char = bb.getChar()
  override def getShort: Short = bb.getShort()
  override def getInt: Int = bb.getInt()
  override def getLong: Long = bb.getLong()
}
