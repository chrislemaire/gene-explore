package com.clemaire.cache.impl.io.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ChunkIndex

class BasicIndexWriter[CI <: ChunkIndex](path: Path)
  extends NioIndexWriter[CI](path)
    with BasicIndexDataWriter[CI]
