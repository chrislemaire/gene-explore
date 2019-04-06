package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.cache.definitions.{Cache, Identifiable}
import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.io.writing.ChunkWriter
import com.clemaire.gexplore.core.gfa.cache.chunk.{PositionalChunkBuilder, PositionalChunkIndexConstructor}
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalIndex}
import com.clemaire.gexplore.core.gfa.Positional

import scala.reflect.ClassTag

class PositionalCache[D <: Identifiable with Positional]
(val writer: ChunkWriter[D],
 override val reader: ChunkReader[D, PositionalChunkIndex],
 override val index: PositionalIndex[PositionalChunkIndex],
 override val max: Int = 5)
(override implicit val D: ClassTag[D])
  extends PositionalReadOnlyCache[D, PositionalChunkIndex](reader, index, max)
    with Cache[D, PositionalChunkIndex] {

  /**
    * The [[ChunkBuilder]] that governs deciding on
    * when a [[com.clemaire.cache.definitions.chunk.Chunk]]
    * is complete and constructs it when it is ready.
    */
  override val chunkBuilder: ChunkBuilder[D, PositionalChunkIndex] =
    new PositionalChunkBuilder[D, PositionalChunkIndex]()
      with PositionalChunkIndexConstructor

  /**
    * Constructs a read-only [[PositionalCache]] from this
    * [[PositionalCache]].
    *
    * @return The constructed [[PositionalReadOnlyCache]].
    */
  override def readOnly: PositionalReadOnlyCache[D, PositionalChunkIndex] =
    new PositionalReadOnlyCache[D, PositionalChunkIndex](reader, index, max)

}
