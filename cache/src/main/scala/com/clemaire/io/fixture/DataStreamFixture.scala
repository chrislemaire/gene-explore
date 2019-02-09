package com.clemaire.io.fixture

import java.io.{DataInputStream, DataOutputStream}

import scala.language.implicitConversions

object DataStreamFixture {
  implicit def dos2fixtureOut(os: DataOutputStream): OutputFixture =
    new DataStreamOutputFixture(os)

  implicit def dos2fixtureIn(os: DataInputStream): InputFixture =
    new DataStreamInputFixture(os)
}

class DataStreamOutputFixture(os: DataOutputStream)
  extends OutputFixture {

  def writeByte(v: Byte): Unit = os.writeByte(v)
  def writeChar(v: Char): Unit = os.writeChar(v)
  def writeShort(v: Short): Unit = os.writeShort(v)
  def writeInt(v: Int): Unit = os.writeInt(v)
  def writeLong(v: Long): Unit = os.writeLong(v)
}

class DataStreamInputFixture(os: DataInputStream)
  extends InputFixture {

  override def getByte: Byte = os.readByte()
  override def getChar: Char = os.readChar()
  override def getShort: Short = os.readShort()
  override def getInt: Int = os.readInt()
  override def getLong: Long = os.readLong()
}
