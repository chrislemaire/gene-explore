package com.clemaire.cache.definitions

import com.clemaire.cache.definitions.chunk.MutableChunk
import com.clemaire.cache.definitions.index.ChunkIndex

trait CacheData[D <: Identifiable, CI <: ChunkIndex]
  extends ReadOnlyCacheData[D, CI] {

  /**
    * The chunk that is currently being built represented
    * as a mapping from data entry ids to data entries.
    *
    * This mapping is needed to lookup currently unwritten
    * data entries.
    */
  protected var currentChunk: MutableChunk[D] = new MutableChunk[D](0)

}
