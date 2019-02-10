package com.clemaire.cache.impl.chunk

import com.clemaire.cache.definitions.chunk.{ChunkBuilder, ChunkIndexConstructor}
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.impl.chunk.capacity.SetBytes

abstract class BasicChunkBuilder[D <: Identifiable, CI <: ChunkIndex](val max: Int = 8192)
  extends ChunkBuilder[D, CI]
    with ChunkIndexConstructor[CI]
    with SetBytes
