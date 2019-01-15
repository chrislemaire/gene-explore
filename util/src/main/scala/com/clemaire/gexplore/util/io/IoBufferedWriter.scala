package com.clemaire.gexplore.util.io

import java.io.{BufferedOutputStream, DataOutputStream, Writer}
import java.nio.file.{Files, Path, StandardOpenOption}

trait IoBufferedWriter extends Writer {

  /**
    * The [[Path]] location of the file to write to.
    */
  private[this] var _path: Path = _

  /**
    * The size of the buffer within this Writer's [[DataOutputStream]].
    */
  private[this] var _size: Int = 8192

  /**
    * The [[DataOutputStream]] to write data to.
    */
  private[this] var _os: DataOutputStream = _

  /**
    * @return The current [[DataOutputStream]] used
    *         to write data to file.
    */
  protected[this] def os: DataOutputStream = _os

  /**
    * Reopens the output stream after closing any existing OS.
    * @return This [[IoBufferedWriter]]
    */
  private[this] def reopenOS(): this.type = {
    if (_os != null) {
      _os.close()
    }
    _os = new DataOutputStream(new BufferedOutputStream(
      Files.newOutputStream(_path,
        StandardOpenOption.WRITE, StandardOpenOption.CREATE), _size))
    this
  }

  /**
    * Sets the path to the file this [[IoBufferedWriter]]
    * should write to.
    *
    * @param path The [[Path]] of the file to write to.
    * @return This [[IoBufferedWriter]].
    */
  protected[this] def withPath(path: Path): this.type = {
    _path = path
    reopenOS()
  }

  /**
    * Sets the buffer-size internal to this [[IoBufferedWriter]].
    * @param size The desired size of the buffer.
    * @return This [[IoBufferedWriter]]
    */
  protected[this] def withBufferSize(size: Int): this.type = {
    _size = size
    reopenOS()
  }

  override def flush(): Unit =
    _os.flush()

  override def close(): Unit =
    _os.close()

}
