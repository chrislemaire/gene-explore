package com.clemaire.gexplore.util.io

import java.io.IOException
import java.nio.{BufferOverflowException, ByteBuffer}
import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}

import com.clemaire.gexplore.util.io.NioBufferedReader.DEFAULT_BUFFER_SIZE

object NioBufferedDataReader {

  /**
    * The default number of bytes to buffer in-memory
    * of the file.
    */
  val DEFAULT_BUFFER_SIZE = 8192

}

/**
  * Buffered reader implementation using java.nio buffering
  * to speed up sequential line reads. Additionally adds a
  * function for getting the current file position to enable
  * creating exact file indexing structures.
  *
  * @param path       The path to the file to read from.
  * @param bufferSize The size of the buffer to use during
  *                   buffered reading.
  * @see java.io.AutoCloseable
  * @see java.io.BufferedReader
  * @author Chris Lemaire
  */
class NioBufferedDataReader(val path: Path,
                            var bufferSize: Int = DEFAULT_BUFFER_SIZE)
  extends AutoCloseable {

  /**
    * The [[FileChannel]] used to read from file.
    */
  private[this] val _fc: FileChannel = FileChannel.open(path, StandardOpenOption.READ)

  /**
    * The currently buffered data.
    */
  private[this] var _buffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)

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
    * Reset the reader upon initial creation.
    */
  private val _: Unit = {
    reset()
  }

  /**
    * Resets the reader to read from the first position
    * in the file.
    *
    * @throws IOException when something goes wrong during
    *                     the initial read from file.
    */
  @throws[IOException]
  def reset(): Unit = {
    this._fc.position(0L)
    this._buffer = ByteBuffer.allocateDirect(bufferSize)
    this._filePos = 0L
    this._eofReached = false

    loadNextBuffer()
  }

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
  private def checkBuffer(margin: Int): Boolean = {
    val eofReached = _eofReached
    if (_buffer.position + margin >= _buffer.limit && !_eofReached)
      loadNextBuffer()
    if (_filePos + margin >= _fc.size)
      _eofReached = true
    eofReached
  }

  /**
    * @return The number of bytes left to read in the
    *         current buffer.
    */
  private def buffBytesLeft(): Int =
    _buffer.limit - _buffer.position

  def readByte: Byte =
    if (!checkBuffer(1))
      _buffer.get
    else
      throw new BufferOverflowException

  def readChar: Char =
    if (!checkBuffer(1))
      _buffer.getChar
    else
      throw new BufferOverflowException

  def readInt: Int =
    if (!checkBuffer(4))
      _buffer.getInt
    else
      throw new BufferOverflowException

  def readLong: Long =
    if (!checkBuffer(8))
      _buffer.getLong
    else
      throw new BufferOverflowException

  /**
    * Calculates the position in file that is currently
    * being pointed to as the next location to read a
    * byte from.
    *
    * @return The current file position.
    */
  def position: Long = _filePos + _buffer.position

  def eofReached: Boolean = _eofReached

  @throws[IOException]
  override def close(): Unit =
    _fc.close()

}
