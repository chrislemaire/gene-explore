package com.clemaire.cache.impl.index

import java.nio.file.Path

import com.clemaire.cache.definitions.index.{ChunkIndex, Index, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.cache.impl.io.writing.{BasicIndexDataWriter, NioIndexWriter}

class BasicIndex(val writer: IndexWriter[ChunkIndex])
  extends Index[ChunkIndex] {

  def this(path: Path) = this(
    new NioIndexWriter[ChunkIndex](path)
      with BasicIndexDataWriter)

  /**
    * Constructs and returns a safe immutable
    * [[ReadOnlyIndex]] for further use.
    *
    * @return Immutable [[ReadOnlyIndex]] for further use.
    */
  override def readOnly: ReadOnlyIndex[ChunkIndex] =
    ReadOnlyIndex[ChunkIndex](this)

}
