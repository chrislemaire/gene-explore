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
  private val fc: FileChannel = FileChannel.open(path, StandardOpenOption.READ)

  /**
    * The currently buffered data.
    */
  private var buffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)

  /**
    * The currently buffered data as a byte array.
    */
  private var bufferArray: Array[Byte] = Array.ofDim(bufferSize)

  /**
    * The current position in the buffer.
    */
  private var bufferPos = 0

  /**
    * The left-most position of the file currently
    * buffered.
    */
  private var filePos = 0L

  /**
    * Whether the end of the file has been reached.
    */
  private var eofReached = false

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
    this.fc.position(0L)
    this.buffer = ByteBuffer.allocateDirect(bufferSize)
    this.bufferArray = new Array[Byte](bufferSize)
    this.filePos = 0L
    this.bufferPos = 0
    this.eofReached = false

    loadNextBuffer()
  }

  /**
    * Loads the next chunk of size [[bufferSize]] into
    * [[buffer]] and [[bufferArray]]. This also
    * increments the file [[filePos pointer]] to be
    * pointing to the left minimum byte position currently
    * read.
    *
    * @throws IOException when something goes wrong during
    *                     reading from file.
    */
  @throws[IOException]
  private def loadNextBuffer(): Unit = {
    filePos += buffer.position
    bufferPos = 0

    buffer.clear
    fc.read(buffer)
    buffer.flip

    buffer.get(bufferArray, 0, Math.min(bufferSize, buffer.limit))
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
    if (bufferPos >= buffer.limit && !eofReached)
      if (filePos + buffer.position >= fc.size)
        eofReached = true
      else
        loadNextBuffer()
    eofReached
  }

  /**
    * Checks whether the end of file is reached and
    * returns whether there are more bytes left to
    * read in the file.
    *
    * @return {{{true}}} when the file still has
    *         more bytes to read, {{{false}}} otherwise.
    */
  def hasNext: Boolean = !eofReached

  @throws[IOException]
  override def read(charBuf: Array[Char], off: Int, len: Int): Int = {
    var currentPos = off

    while (currentPos < len + off) {
      if (checkBuffer())
        return currentPos - off

      val readUntil = Math.min(buffer.limit, bufferPos + len - currentPos)
      while (bufferPos < readUntil) {
        charBuf(currentPos) = bufferArray(bufferPos).asInstanceOf[Char]

        bufferPos += 1
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
      start = bufferPos
      val limit = buffer.limit

      var i = bufferPos
      while (i < limit) {
        c = bufferArray(i)
        if (c == '\r') {
          lineBuilder.append(new String(
            bufferArray, start, i - start))
          foundCR = true
          start = i + 1
        } else if (c == '\n') {
          if (!foundCR) {
            lineBuilder.append(new String(
              bufferArray, start, i - start - 1))
          }
          bufferPos = i + 1
          return lineBuilder.toString()
        } else if (foundCR) {
          bufferPos = i
          return lineBuilder.toString()
        }

        i += 1
      }

      bufferPos = limit
      lineBuilder.append(new String(
        bufferArray, start, i - start))
      if (checkBuffer()) {
        return lineBuilder.toString()
      }
    }

    lineBuilder.toString()
  }

  /**
    * Calculates the position in file that is currently
    * being pointed to as the next location to read a
    * byte from.
    *
    * @return The current file position.
    */
  def position: Long = filePos + bufferPos

  @throws[IOException]
  override def close(): Unit = {
    fc.close()
  }
}
