package com.clemaire.cache.impl.io.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.io.writing.NioBufferedWriter

abstract class NioIndexWriter[CI <: ChunkIndex](val path: Path)
  extends NioBufferedWriter
    with IndexWriter[CI] {

  /**
    * Writes a give [[ChunkIndex]] to the underlying
    * source for this [[IndexWriter]].
    *
    * @param ci The [[ChunkIndex]] to write.
    */
  override def write(ci: CI): Unit = {
    checkForFlush(length)
    writeData(ci, this)
  }

}
