package com.clemaire.cache.impl

import java.nio.file.Path

import com.clemaire.cache.definitions.{Cache, Identifiable, ReadOnlyCache}
import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.cache.definitions.index.{ChunkIndex, Index}
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.io.writing.ChunkWriter
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.chunk.{BasicChunkBuilder, BasicChunkIndexConstructor}
import com.clemaire.cache.impl.index.BasicIndex
import com.clemaire.cache.impl.scheduling.LRU

import scala.reflect.ClassTag

class BasicCache[D <: Identifiable]
(val writer: ChunkWriter[D],
 override val reader: ChunkReader[D, ChunkIndex],
 override val index: Index[ChunkIndex],
 override val max: Int = 25)
(override implicit val D: ClassTag[D])
  extends Cache[D, ChunkIndex]
    with SetNumberOfChunks[D, ChunkIndex]
    with LRU[D, ChunkIndex] {

  def this(writer: ChunkWriter[D],
           reader: ChunkReader[D, ChunkIndex],
           indexPath: Path)
          (implicit D: ClassTag[D]) =
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
    new BasicReadOnlyCache[D](reader, index.readOnly, max)

}
