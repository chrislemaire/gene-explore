package com.clemaire.gexplore.core.gfa.reference.genome.cache

import java.nio.file.Path

import com.clemaire.cache.definitions.{Cache, ReadOnlyCache}
import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.cache.definitions.index.Index
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.io.writing.ChunkWriter
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.index.BasicIndex
import com.clemaire.cache.impl.scheduling.LRU
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.genome.cache.chunk.GCChunkBuilder
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex
import com.clemaire.gexplore.core.gfa.reference.genome.cache.reading.GCReader
import com.clemaire.gexplore.core.gfa.reference.genome.cache.writing.{GCIndexWriter, GCWriter}

class GCCache
(val writer: ChunkWriter[GenomeCoordinate],
 override val reader: ChunkReader[GenomeCoordinate, GCChunkIndex],
 override val index: Index[GCChunkIndex],
 n: Int,
 override val max: Int = 25)
  extends Cache[GenomeCoordinate, GCChunkIndex]
    with SetNumberOfChunks[GenomeCoordinate, GCChunkIndex]
    with LRU[GenomeCoordinate, GCChunkIndex] {

  /**
    * The [[ChunkBuilder]] that governs deciding on
    * when a [[com.clemaire.cache.definitions.chunk.Chunk]]
    * is complete and constructs it when it is ready.
    */
  override val chunkBuilder: ChunkBuilder[GenomeCoordinate, GCChunkIndex] =
    new GCChunkBuilder(n, max = 1024 * 1024)

  /**
    * Constructs a read-only [[Cache]] from this
    * [[Cache]].
    *
    * @return The constructed [[ReadOnlyCache]].
    */
  override def readOnly: GCReadOnlyCache =
    new GCReadOnlyCache(reader, index.readOnly, max)

}


object GCCache {
  def apply(dataPath: Path,
            indexPath: Path,
            n: Int): GCCache =
    new GCCache(
      writer = new GCWriter(dataPath),
      reader = new GCReader(dataPath, n),
      index = new BasicIndex(new GCIndexWriter(indexPath, n)),
      n)

}
