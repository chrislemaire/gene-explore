package com.clemaire.gexplorer.core.gfa.reference

import java.io.{BufferedOutputStream, DataOutputStream}
import java.nio.file.{Files, Path, StandardOpenOption}

trait IoBufferedWriter
  extends ReferenceWriter {

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
    * Sets the path to the file this [[IoBufferedWriter]]
    * should write to.
    *
    * @param path The [[Path]] of the file to write to.
    */
  protected[this] def withPath(path: Path): this.type = {
    if (_os != null) {
      _os.close()
    }
    _os = new DataOutputStream(new BufferedOutputStream(
      Files.newOutputStream(path,
        StandardOpenOption.WRITE, StandardOpenOption.CREATE)))
    this
  }

  override def flush(): Unit =
    _os.flush()

  override def close(): Unit =
    _os.close()

}
