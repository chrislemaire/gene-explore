package com.clemaire.cache.impl

import java.nio.file.Path

import com.clemaire.cache.definitions.{Cache, Identifiable, ReadOnlyCache}
import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.cache.definitions.index.{ChunkIndex, Index}
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.io.writing.ChunkWriter
import com.clemaire.cache.impl.chunk.{BasicChunkBuilder, BasicChunkIndexConstructor}
import com.clemaire.cache.impl.index.BasicIndex

class BasicCache[D <: Identifiable]
(val writer: ChunkWriter[D],
 _reader: ChunkReader[D, ChunkIndex],
 override val index: Index[ChunkIndex],
 override val max: Int = 25)
  extends BasicReadOnlyCache[D](_reader, index)
    with Cache[D, ChunkIndex] {

  def this(writer: ChunkWriter[D], reader: ChunkReader[D, ChunkIndex], indexPath: Path) =
    this(writer, reader, new BasicIndex[ChunkIndex](indexPath))

  /**
    * The [[ChunkBuilder]] that governs deciding on
    * when a [[com.clemaire.cache.definitions.chunk.Chunk]]
    * is complete and constructs it when it is ready.
    */
  override val chunkBuilder: ChunkBuilder[D, ChunkIndex] =
    new BasicChunkBuilder[D, ChunkIndex]
      with BasicChunkIndexConstructor

  /**
    * Constructs a read-only [[Cache]] from this
    * [[Cache]].
    *
    * @return The constructed [[ReadOnlyCache]].
    */
  override def readOnly: ReadOnlyCache[D, ChunkIndex] =
    new BasicReadOnlyCache[D](reader, index, max)

}
