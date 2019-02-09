package com.clemaire.cache.definitions.chunk

import com.clemaire.cache.definitions.index.ChunkIndex

trait ChunkIndexConstructor[CI <: ChunkIndex]
  extends Object
    with ChunkBuilderData {

  /**
    * Constructs a new [[ChunkIndex]] of type [[CI]] from
    * the current state.
    *
    * @return The built [[ChunkIndex]].
    */
  protected[this] def constructChunkIndex: CI

}
