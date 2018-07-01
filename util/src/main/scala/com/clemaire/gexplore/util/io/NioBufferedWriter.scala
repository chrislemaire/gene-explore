package com.clemaire.gexplore.util.io

import java.io.Writer
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}

trait NioBufferedWriter
  extends Writer {

  /**
    * The [[FileChannel]] used to write the current
    * buffer to a file, or [[None]] if no file
    * is selected yet.
    */
  private[this] var fc: Option[FileChannel] = None

  /**
    * The size of the buffer to allocate and use.
    */
  protected[this] var bufferSize: Int = 1024 * 1024

  /**
    * The buffer that is directly allocated to serve
    * as a intermediate medium between the processing
    * and the disk.
    */
  protected[this] var buffer: ByteBuffer =
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
  protected def flushBuffer(): Unit = if (buffer.position() > 0) {
    buffer.flip()
    fc.get.write(buffer)
    buffer.clear()
  }

  /**
    * Reallocates the buffer with the given new size as its
    * size and sets the [[bufferSize]] field accordingly.
    *
    * @param newBufferSize The new buffer size.
    */
  private[this] def reallocateBuffer(newBufferSize: Int): Unit = {
    bufferSize = newBufferSize
    buffer = ByteBuffer.allocateDirect(newBufferSize)
  }

  /**
    * Redefines the [[FileChannel]] by opening one for the
    * file at the given path and closing any currently open
    * [[FileChannel]]s.
    *
    * @param file The [[Path]] at which the file to open is
    *             located.
    */
  protected[this] def withPath(file: Path): Unit = {
    fc.foreach(_.close())
    fc = Some(FileChannel.open(file, StandardOpenOption.WRITE,
      StandardOpenOption.CREATE))
  }

  /**
    * Reallocates the buffer with the given new size as its
    * size and sets the [[bufferSize]] field accordingly.
    * Returns the new [[NioBufferedWriter]] as the result.
    *
    * @param newBufferSize The new buffer size.
    * @return The new [[NioBufferedWriter]].
    */
  def withBufferSize(newBufferSize: Int): this.type = {
    reallocateBuffer(newBufferSize)
    this
  }

  /**
    * Redefines the [[FileChannel]] by opening one for the
    * file at the given path and closing any currently open
    * [[FileChannel]]s.
    * Returns the new [[NioBufferedWriter]] as the result.
    *
    * @param file The [[Path]] at which the file to open is
    *             located.
    * @return The new [[NioBufferedWriter]].
    */
  def forFileAt(file: Path): this.type = {
    withPath(file)
    this
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

  override def flush(): Unit =
    flushBuffer()

  override def close(): Unit =
    fc.foreach(_.close())

}
