package com.clemaire.cache.impl.index

import com.clemaire.cache.definitions.index.ChunkIndex
import com.lodborg.intervaltree.IntegerInterval

case class BasicChunkIndex(id: Int,
                           filePos: Long,
                           length: Long,
                           ids: IntegerInterval)
  extends ChunkIndex
