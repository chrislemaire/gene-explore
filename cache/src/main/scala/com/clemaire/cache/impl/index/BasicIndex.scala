package com.clemaire.cache.impl.index

import java.nio.file.Path

import com.clemaire.cache.definitions.index.{ChunkIndex, Index, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.cache.impl.io.writing.{BasicIndexDataWriter, NioIndexWriter}

class BasicIndex[CI <: ChunkIndex](val writer: IndexWriter[CI])
  extends Index[CI] {

  def this(path: Path) = this(
    new NioIndexWriter[CI](path)
      with BasicIndexDataWriter[CI])

  /**
    * Constructs and returns a safe immutable
    * [[ReadOnlyIndex]] for further use.
    *
    * @return Immutable [[ReadOnlyIndex]] for further use.
    */
  override def readOnly: ReadOnlyIndex[CI] =
    ReadOnlyIndex[CI](this)

}
