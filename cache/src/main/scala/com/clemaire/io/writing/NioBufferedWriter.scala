package com.clemaire.io.writing

import java.io.Writer
import java.nio.channels.FileChannel
import java.nio.ByteBuffer
import java.nio.file.{Path, StandardOpenOption}

import com.clemaire.io.fixture.OutputFixture

trait NioBufferedWriter
  extends Writer
    with OutputFixture {

  /**
    * The [[Path]] to the file to write to.
    */
  protected[this] val path: Path

  /**
    * The size of the buffer to allocate and use.
    */
  protected[this] val bufferSize: Int = 32 * 1024

  /**
    * The [[FileChannel]] used to write the current
    * buffer to a file, or [[None]] if no file
    * is selected yet.
    */
  private[this] val fc: FileChannel = FileChannel.open(path,
    StandardOpenOption.CREATE, StandardOpenOption.WRITE)

  /**
    * The buffer that is directly allocated to serve
    * as a intermediate medium between the processing
    * and the disk.
    */
  protected[this] val buffer: ByteBuffer =
    ByteBuffer.allocateDirect(bufferSize)

  override def write(charBuf: Array[Char],
                     off: Int,
                     len: Int): Unit = {
    var arrPos = off
    while (arrPos < off + len) {
      checkForFlush(len)

      val pos = buffer.position()
      val limit = buffer.limit()
      for (_ <- pos until limit) {
        buffer.put(charBuf(arrPos).toByte)
        arrPos += 1
      }
    }
  }

  /**
    * Flushes the current buffer to the current [[FileChannel]]
    * in a blocking fashion after confirming the buffer has
    * content.
    */
  protected def flushBuffer(): Unit =
    if (buffer.position() > 0) {
      buffer.flip()
      fc.write(buffer)
      buffer.clear()
    }

  /**
    * Checks whether a flush is necessary and, if so, flushes
    * the current buffer. This should be necessary before
    * writing to file.
    *
    * @param nextLength The length of the data that the caller
    *                   is trying to write to buffer.
    */
  def checkForFlush(nextLength: Int): Unit =
    if (buffer.position() + nextLength >= buffer.capacity()) {
      flushBuffer()
    }

  /**
    * Writes a byte to the underlying source.
    *
    * @param v The byte to write.
    */
  override def writeByte(v: Byte): Unit =
    buffer.put(v)

  /**
    * Writes a character to the underlying source.
    *
    * @param v The Char to write.
    */
  override def writeChar(v: Char): Unit =
    buffer.putChar(v)

  /**
    * Writes a short to the underlying source.
    *
    * @param v The Short to write.
    */
  override def writeShort(v: Short): Unit =
    buffer.putShort(v)

  /**
    * Writes an integer to the underlying source.
    *
    * @param v The Int to write.
    */
  override def writeInt(v: Int): Unit =
    buffer.putInt(v)

  /**
    * Writes a long to the underlying source.
    *
    * @param v The Long to write.
    */
  override def writeLong(v: Long): Unit =
    buffer.putLong(v)

  override def flush(): Unit =
    flushBuffer()

  override def close(): Unit =
    fc.close()

}
