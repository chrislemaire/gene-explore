package com.clemaire.gexplore.util.io

import java.io.Reader
import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}
import java.nio.file.{Path, StandardOpenOption}
import java.util.concurrent.{CountDownLatch, TimeUnit}

import com.clemaire.gexplore.util.Stopwatch

trait AsyncNioBufferedLinearReader
  extends Reader {

  /**
    * The EOL character as defined by the OS
    * of the user.
    */
  private val EOL: Char = {
    val os = System.getProperty("os.name").toLowerCase()
    if (os.contains("mac")) {
      '\r'
    } else {
      '\n'
    }
  }

  /**
    * The file channel used for reading.
    */
  private var _fc: Option[AsynchronousFileChannel] = None

  /**
    * The lock noting that the right buffer is currently
    * being loaded.
    */
  private var bufferLock: CountDownLatch = _

  /**
    * The left buffer in which the position pointer
    * is currently located.
    */
  private var current: ByteBuffer = _

  /**
    * The buffer loaded to next be used to read from.
    */
  private var next: ByteBuffer = _

  /**
    * The size of the buffers used in this
    * [[AsyncNioBufferedLinearReader]].
    */
  private var bufferSize: Int = 1024 * 1024

  /**
    * The left-most position in the file currently read
    * in this [[AsyncNioBufferedLinearReader]]'s buffers.
    */
  private var low: Long = 0

  /**
    * @return Whether the current buffer is entirely read.
    */
  private[this] def nonRemaining: Boolean = !current.hasRemaining

  /**
    * @return Whether the end of file is reached.
    */
  private[this] def eof: Boolean = current == null

  /**
    * CompletionHandler that deals with completing a 'next'
    * buffer read. This unlocks the lock that is set upon
    * read request.
    */
  private class BufferReadHandler
    extends CompletionHandler[Integer, ByteBuffer] {

    override def completed(result: Integer,
                           attachment: ByteBuffer): Unit = {
      attachment.flip()
      if (bufferLock != null)
        bufferLock.countDown()
    }

    override def failed(exc: Throwable, attachment: ByteBuffer): Unit = {
      current = null
      next = null
      if (bufferLock != null)
        bufferLock.countDown()
      exc.printStackTrace()
    }
  }

  protected def withBufferSize(bufferSize: Int): this.type = {
    this.bufferSize = bufferSize
    this
  }

  protected def forFile(path: Path): this.type = {
    _fc.foreach(_.close())
    _fc = Some(AsynchronousFileChannel.open(path,
      StandardOpenOption.READ))

    low = 0
    current = ByteBuffer.allocateDirect(bufferSize)
    next = null

    _fc.foreach(_.read(current, 0, current, new BufferReadHandler))

    _fc.filter(bufferSize < _.size())
      .foreach(fc => {
        next = ByteBuffer.allocateDirect(bufferSize)
        fc.read(next, bufferSize, next, new BufferReadHandler)
      })

    this
  }

  /**
    * Swaps the buffers and starts an asynchronous read for
    * a new portion of the file if necessary.
    */
  private[this] def swapAndUpdateBuffers(): Unit = {
    Stopwatch.start("swap")

    val length = current.limit()
    val temp = current
    current.clear()

    current = next
    next = null

    low += length
    if (current != null) {
      _fc.filter(_.size() > low + current.limit())
        .foreach(fc => {
          if (bufferLock != null)
            bufferLock.await(30L, TimeUnit.SECONDS)
          bufferLock = new CountDownLatch(1)

          fc.read(temp, low + current.limit(), temp, new BufferReadHandler)
          next = temp
        })
    }
    Stopwatch.stop("swap")
  }

  /**
    * Checks whether the 'current' buffer is entirely read
    * and, if so, swaps and updates the buffers.
    */
  private[this] def checkForFill(): Unit =
    if (current != null && nonRemaining)
      swapAndUpdateBuffers()

  /**
    * Reads bytes as characters until the given buffer is
    * full or EOF is reached.
    *
    * @param cBuf The character buffer to fill,
    * @param from The index from which to read.
    * @return The number of characters read in total.
    */
  def read(cBuf: Array[Char],
           from: Int = 0): Int = {
    val length = math.min(current.limit() - current.position(),
      cBuf.length - from)
    for (i <- from until from + length) {
      cBuf(i) = current.getChar
    }

    if (current.position() >= current.limit()
      && next != null) {

      swapAndUpdateBuffers()
      read(cBuf, from + length)
    } else {
      from + length
    }
  }

  /**
    * Reads a line of the buffered input. When needed, this
    * function will refill the buffer. When EOF is reached
    * and some characters were left in the buffer before EOF,
    * these characters are accumulated as a String and returned.
    * When EOF is reached immediately, an empty option is returned.
    *
    * @param maxLength The maximum number of bytes to read as a
    *                  single line.
    * @return An option either containing the read line as a
    *         String or nothing when EOF is reached immediately.
    */
  def readLine(maxLength: Int = 1024 * 1024): Option[String] = {
    Stopwatch.start("read")
    Stopwatch.start("read init")
    val strBuff = new StringBuffer(1024)

    checkForFill()

    var firstPass = true
    Stopwatch.stop("read init")
    while (strBuff.length() < maxLength) {
      Stopwatch.start("read eof check")
      // If the buffer is empty, quit.
      if (eof) {
        if (firstPass) {
          return None
        } else {
          return Some(strBuff.toString)
        }
      }
      Stopwatch.stop("read eof check")

      Stopwatch.start("read loop")
      // Add characters that are not EOL to the string buffer.
      for (_ <- 0 until current.limit() - current.position()) {
        Stopwatch.start("read loop - get byte")
        val c: Byte = current.get()
        Stopwatch.stop("read loop - get byte")
        if (c == EOL) {
          Stopwatch.stop("read loop")
          return Some(strBuff.toString)
        } else if (c != '\r') {
          strBuff.append(c)
        }
      }
      Stopwatch.stop("read loop")

      Stopwatch.start("read swap")
      firstPass = false
      swapAndUpdateBuffers()
      Stopwatch.stop("read swap")
    }

    Some(strBuff.toString)
  }

  /**
    * @return Whether this [[Reader]] still has bytes to read
    *         remaining.
    */
  def hasRemaining: Boolean = !eof

  override def read(cBuf: Array[Char], off: Int, len: Int): Int =
    read(cBuf, -0)

  override def close(): Unit =
    _fc.foreach(_.close())
}
