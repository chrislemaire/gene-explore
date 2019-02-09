package com.clemaire.io.writing

import java.io.{BufferedOutputStream, DataOutputStream, Writer}
import java.nio.file.{Files, Path, StandardOpenOption}

import com.clemaire.io.fixture.OutputFixture

trait IoBufferedWriter
  extends Writer
    with OutputFixture {

  /**
    * The [[Path]] location of the file to write to.
    */
  protected[this] val path: Path

  /**
    * The size of the buffer within this Writer's [[DataOutputStream]].
    */
  protected[this] val size: Int = 8192

  /**
    * The [[DataOutputStream]] to write data to.
    */
  protected[this] val os: DataOutputStream =
    new DataOutputStream(new BufferedOutputStream(
      Files.newOutputStream(path,
        StandardOpenOption.WRITE, StandardOpenOption.CREATE),
      size
    ))

  override def write(cbuf: Array[Char], off: Int, len: Int): Unit =
    os.write(cbuf.map(_.toByte), off, len)

  /**
    * Writes a byte to the underlying source.
    *
    * @param v The byte to write.
    */
  override def writeByte(v: Byte): Unit =
    os.write(v)

  /**
    * Writes a character to the underlying source.
    *
    * @param v The Char to write.
    */
  override def writeChar(v: Char): Unit =
    os.writeChar(v)

  /**
    * Writes a short to the underlying source.
    *
    * @param v The Short to write.
    */
  override def writeShort(v: Short): Unit =
    os.writeShort(v)

  /**
    * Writes an integer to the underlying source.
    *
    * @param v The Int to write.
    */
  override def writeInt(v: Int): Unit =
    os.writeInt(v)

  /**
    * Writes a long to the underlying source.
    *
    * @param v The Long to write.
    */
  override def writeLong(v: Long): Unit =
    os.writeLong(v)

  override def flush(): Unit =
    os.flush()

  override def close(): Unit =
    os.close()

}
