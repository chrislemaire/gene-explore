package com.clemaire.gexplore.util.io

import java.io.{IOException, Reader}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}

import com.clemaire.gexplore.util.io.NioBufferedReader.DEFAULT_BUFFER_SIZE

object NioBufferedReader {

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
  * @see java.io.Reader
  * @see java.io.BufferedReader
  * @author Chris Lemaire
  */
class NioBufferedReader(val path: Path,
                        var bufferSize: Int = DEFAULT_BUFFER_SIZE)
  extends Reader {

  /**
    * The [[FileChannel]] used to read from file.
    */
  private[this] val _fc: FileChannel = FileChannel.open(path, StandardOpenOption.READ)

  /**
    * The currently buffered data.
    */
  private[this] var _buffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)

  /**
    * The currently buffered data as a byte array.
    */
  private[this] var _bufferArray: Array[Byte] = Array.ofDim(bufferSize)

  /**
    * The current position in the buffer.
    */
  private[this] var _bufferPos = 0

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
  override def reset(): Unit = {
    this._fc.position(0L)
    this._buffer = ByteBuffer.allocateDirect(bufferSize)
    this._bufferArray = new Array[Byte](bufferSize)
    this._filePos = 0L
    this._bufferPos = 0
    this._eofReached = false

    loadNextBuffer()
  }

  /**
    * Loads the next chunk of size [[bufferSize]] into
    * [[_buffer]] and [[_bufferArray]]. This also
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
    _bufferPos = 0

    _buffer.clear
    _fc.read(_buffer)
    _buffer.flip

    _buffer.get(_bufferArray, 0, Math.min(bufferSize, _buffer.limit))
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
  private def checkBuffer() = {
    if (_bufferPos >= _buffer.limit && !_eofReached)
      if (_filePos + _buffer.position >= _fc.size)
        _eofReached = true
      else
        loadNextBuffer()
    _eofReached
  }

  /**
    * Checks whether the end of file is reached and
    * returns whether there are more bytes left to
    * read in the file.
    *
    * @return {{{true}}} when the file still has
    *         more bytes to read, {{{false}}} otherwise.
    */
  def hasNext: Boolean = !_eofReached

  @throws[IOException]
  override def read(charBuf: Array[Char], off: Int, len: Int): Int = {
    var currentPos = off

    while (currentPos < len + off) {
      if (checkBuffer())
        return currentPos - off

      val readUntil = Math.min(_buffer.limit, _bufferPos + len - currentPos)
      while (_bufferPos < readUntil) {
        charBuf(currentPos) = _bufferArray(_bufferPos).asInstanceOf[Char]

        _bufferPos += 1
        currentPos += 1
      }
    }
    currentPos - off
  }

  /**
    * Reads a single line from the file as a String
    * and returns the String that represents that line
    * excluding EOL characters. The file read starts
    * where any last reads stopped.
    *
    * @return The String read from the file representing
    *         a single line of the file.
    * @throws IOException when something goes wrong during
    *                     reading from file.
    */
  @throws[IOException]
  def readLine: String = {
    val lineBuilder = new StringBuilder
    checkBuffer()

    var start = 0
    var c = 0
    var foundCR = false

    while (true) {
      start = _bufferPos
      val limit = _buffer.limit

      var i = _bufferPos
      while (i < limit) {
        c = _bufferArray(i)
        if (c == '\r') {
          lineBuilder.append(new String(
            _bufferArray, start, i - start))
          foundCR = true
          start = i + 1
        } else if (c == '\n') {
          if (!foundCR) {
            lineBuilder.append(new String(
              _bufferArray, start, i - start - 1))
          }
          _bufferPos = i + 1
          return lineBuilder.toString()
        } else if (foundCR) {
          _bufferPos = i
          return lineBuilder.toString()
        }

        i += 1
      }

      _bufferPos = limit
      lineBuilder.append(new String(
        _bufferArray, start, i - start))
      if (checkBuffer()) {
        return lineBuilder.toString()
      }
    }

    lineBuilder.toString()
  }

  /**
    * Creates an iterable that can be iterated over to
    * get all lines in the files in order of occurrence.
    * The stream closes
    *
    * @return The Stream of lines in the file to be read.
    */
  def lines(): Iterable[String] =
    Stream.from(0)
      .takeWhile(_ => this.hasNext)
      .map(_ => this.readLine)

  /**
    * Calculates the position in file that is currently
    * being pointed to as the next location to read a
    * byte from.
    *
    * @return The current file position.
    */
  def position: Long = _filePos + _bufferPos

  @throws[IOException]
  override def close(): Unit = {
    _fc.close()
  }
}
