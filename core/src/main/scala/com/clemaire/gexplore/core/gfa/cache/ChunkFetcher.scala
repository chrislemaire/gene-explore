package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.gexplore.core.gfa.reference.index.ChunkIndex

trait ChunkFetcher[C <: Chunk[CI, _], CI <: ChunkIndex]
  extends Object
    with CacheData[C] {

  val reader: ChunkReader[C, CI]

  /**
    * Calls the appropriate functions to fetch chunks from
    * disk and returns them as a mapping of their respective
    * chunk ids mapped to the chunks themselves.
    *
    * @param chunks The indexes of the chunks to load into memory.
    * @return A mapping of chunk ids to chunks.
    */
  protected def fetch(chunks: Set[CI]): Map[Int, C] =
    reader.readChunks(chunks)

}
