package com.clemaire.cache.impl.chunk

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.Identifiable

case class BasicChunk[D <: Identifiable](id: Int,
                                         data: Map[Int, D])
  extends Chunk[D]
