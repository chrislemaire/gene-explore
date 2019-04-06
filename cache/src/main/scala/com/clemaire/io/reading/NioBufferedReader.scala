package com.clemaire.io.reading

import java.io.IOException
import java.nio.{BufferOverflowException, ByteBuffer}
import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}

import com.clemaire.io.fixture.InputFixture

/**
  * Buffered reader implementation using java.nio buffering
  * to speed up sequential line reads. Additionally adds a
  * function for getting the current file position to enable
  * creating exact file indexing structures.
  *
  * @see java.io.AutoCloseable
  * @see java.io.BufferedReader
  * @author Chris Lemaire
  */
trait NioBufferedReader
  extends AutoCloseable
    with InputFixture {

  /**
    * The path to the file to read from.
    */
  protected[this] val path: Path

  /**
    * The size of the buffer to use during buffered
    * reading.
    */
  protected[this] val bufferSize: Int = 8192

  /**
    * The [[FileChannel]] used to read from file.
    */
  private[this] val _fc: FileChannel =
    FileChannel.open(path, StandardOpenOption.READ)

  /**
    * The currently buffered data.
    */
  private[this] val _buffer: ByteBuffer =
    ByteBuffer.allocateDirect(bufferSize)

  /**
    * The left-most position of the file currently
    * buffered.
    */
  private[this] var _filePos = 0L

  /**
    * Whether the end of the file has been reached.
    */
  private[this] var _eofReached = false

  /**
    * Calculates the position in file that is currently
    * being pointed to as the next location to read a
    * byte from.
    *
    * @return The current file position.
    */
  def position: Long = _filePos + _buffer.position

  /**
    * @return `true` when the end of the file is reached,
    *         `false` otherwise.
    */
  def eofReached: Boolean = _eofReached

  private val _: Unit = loadNextBuffer()

  /**
    * Loads the next chunk of size [[bufferSize]] into
    * [[_buffer]]. This also
    * increments the file [[_filePos pointer]] to be
    * pointing to the left minimum byte position currently
    * read.
    *
    * @throws IOException when something goes wrong during
    *                     reading from file.
    */
  @throws[IOException]
  private def loadNextBuffer(): Unit = {
    _filePos += _buffer.position

    _buffer.clear
    _fc.read(_buffer, _filePos)
    _buffer.flip
  }

  /**
    * Checks whether the buffer is fully read and, if so,
    * goes on to check whether a buffer reload is needed.
    * A buffer load is not needed when the end of a file
    * has been reached.
    *
    * @return { @code true} when the end of a file has been
    *         reached, { @code false} otherwise.
    * @throws IOException when something goes wrong during
    *                     reading from file.
    */
  @throws[IOException]
  protected[this] def checkBuffer(margin: Int): Boolean = {
    val eofReached = _eofReached
    if (_buffer.position + margin >= _buffer.limit && !_eofReached)
      loadNextBuffer()
    if (_filePos + margin >= _fc.size)
      _eofReached = true
    eofReached
  }

  /**
    * Skips the given  number of bytes in the input.
    *
    * @param nBytes The number of bytes to skip.
    */
  override def skip(nBytes: Int): Unit =
    if (!checkBuffer(nBytes))
      _buffer.position(_buffer.position + nBytes)
    else
      throw new BufferOverflowException

  /**
    * Reads the next byte and returns it.
    *
    * @return The next read Byte.
    */
  override def getByte: Byte =
    if (!checkBuffer(1))
      _buffer.get
    else
      throw new BufferOverflowException

  /**
    * Reads the next character and returns it.
    *
    * @return The next read Char.
    */
  override def getChar: Char =
    if (!checkBuffer(1))
      _buffer.getChar
    else
      throw new BufferOverflowException

  /**
    * Reads the next short and returns it.
    *
    * @return The next read Short.
    */
  override def getShort: Short =
    if (!checkBuffer(2))
      _buffer.getShort
    else
      throw new BufferOverflowException

  /**
    * Reads the next integer and returns it.
    *
    * @return The next read Int.
    */
  override def getInt: Int =
    if (!checkBuffer(4))
      _buffer.getInt
    else
      throw new BufferOverflowException

  /**
    * Reads the next long and returns it.
    *
    * @return The next read Long.
    */
  override def getLong: Long =
    if (!checkBuffer(8))
      _buffer.getLong
    else
      throw new BufferOverflowException

  @throws[IOException]
  override def close(): Unit =
    _fc.close()

}
