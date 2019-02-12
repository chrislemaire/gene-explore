package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy

import java.nio.file.Path

import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.gexplore.core.gfa.cache.{PositionalCache, PositionalReadOnlyCache}
import com.clemaire.gexplore.core.gfa.cache.index.{PositionalChunkIndex, PositionalIndex}
import com.clemaire.gexplore.core.gfa.coordinates.dummy.DummyNode
import com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy.DummyNodeReadOnlyCache.DummyNodeReadOnlyCache

object DummyNodeCache {
  type DummyNodeCache = PositionalCache[DummyNode]

  def apply(dataPath: Path, indexPath: Path): DummyNodeCache =
    new DummyNodeCache(
      new DummyNodeWriter(dataPath),
      new DummyNodeReader(dataPath),
      PositionalIndex(indexPath)
    ) {

      /**
        * The [[ChunkBuilder]] that governs deciding on
        * when a [[com.clemaire.cache.definitions.chunk.Chunk]]
        * is complete and constructs it when it is ready.
        */
      override val chunkBuilder: ChunkBuilder[DummyNode, PositionalChunkIndex] =
        new DummyNodeChunkBuilder()

      /**
        * Constructs a read-only [[PositionalCache]] from this
        * [[PositionalCache]].
        *
        * @return The constructed [[PositionalReadOnlyCache]].
        */
      override def readOnly: DummyNodeReadOnlyCache =
        new DummyNodeReadOnlyCache(reader, index, max)

    }

}
