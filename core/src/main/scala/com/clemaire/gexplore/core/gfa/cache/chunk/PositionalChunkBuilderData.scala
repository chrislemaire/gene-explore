package com.clemaire.gexplore.core.gfa.cache.chunk

import com.clemaire.cache.definitions.chunk.ChunkBuilderData
import com.clemaire.interval.IntInterval

trait PositionalChunkBuilderData
  extends ChunkBuilderData {

  /**
    * The ids currently covered by the chunk.
    */
  protected[this] var layers: IntInterval =
    new IntInterval(Int.MaxValue, Int.MinValue)

}
