package com.clemaire.gexplorer.core.gfa.reference

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.{Path, StandardOpenOption}

import scala.collection.mutable

trait AsyncNioBufferedWriter[T]
  extends NioBufferedWriter {

  /**
    * The [[AsynchronousFileChannel]] used to write the
    * current buffer to a file, or [[None]] if no file
    * is selected yet.
    */
  private[this] var fc: Option[AsynchronousFileChannel] = None

  /**
    * The file position we should currently write to.
    * Necessary to have as an argument to asynchronous
    * writes.
    */
  private[this] var filePos: Long = 0

  /**
    * The buffer of all work that is supposed to be
    * executed during asynchronous writes.
    */
  protected[this] val workBuffer: mutable.Buffer[T] =
    mutable.Buffer()

  /**
    * Adds work to the buffer of work for this
    * [[AsyncNioBufferedWriter]].
    *
    * @param work The work to add to the buffer of work.
    */
  protected[this] def addWork(work: T): Unit = {
    workBuffer += work
  }

  /**
    * Accepts the given work by executing the job
    * associated to that work.
    *
    * @param work The work to accept.
    */
  protected[this] def acceptWork(work: T): Unit

  /**
    * Accepts all work in the work queue by calling
    * [[acceptWork(t)]] for each piece of work and
    * clearing the work buffer.
    */
  def acceptAllWork(): Unit = {
    workBuffer.foreach(w => acceptWork(w))
    workBuffer.clear()
  }

  override def withPath(file: Path): Unit = {
    fc.foreach(_.close())
    fc = Some(AsynchronousFileChannel.open(file,
      StandardOpenOption.WRITE, StandardOpenOption.CREATE))
  }

  override protected def flushBuffer(): Unit = {
    val length = buffer.position()

    buffer.flip()
    fc.foreach(_.write(buffer, filePos))

    buffer = ByteBuffer.allocateDirect(bufferSize)

    filePos += length

    acceptAllWork()
  }
}
