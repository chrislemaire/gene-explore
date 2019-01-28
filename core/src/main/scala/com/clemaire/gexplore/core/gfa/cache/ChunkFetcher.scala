package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

trait ChunkFetcher[I <: AbstractIndex[CI], CI <: ChunkIndex, D]
  extends Object
    with CacheData[Chunk[CI, D]] {

  val reader: ChunkReader[I, CI, D]

  /**
    * Calls the appropriate functions to fetch chunks from
    * disk and returns them as a mapping of their respective
    * chunk ids mapped to the chunks themselves.
    *
    * @param chunks The indexes of the chunks to load into memory.
    * @return A mapping of chunk ids to chunks.
    */
  protected def fetch(chunks: Set[CI]): Map[Int, Chunk[CI, D]] =
    reader.readChunks(chunks)

}
