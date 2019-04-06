package com.clemaire.cache.impl.io.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.io.writing.ChunkWriter
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.io.writing.NioBufferedWriter

abstract class NioChunkWriter[D <: Identifiable](val path: Path)
  extends NioBufferedWriter
    with ChunkWriter[D] {

  /**
    * Writes the given data entry to the underlying
    * source.
    *
    * @param data The data to write to a source.
    */
  override def write(data: D): Unit = {
    checkForFlush(length)
    writeData(data, this)
  }

}
