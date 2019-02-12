package com.clemaire.cache.impl.io.writing

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ChunkIndex

class BasicIndexWriter(path: Path)
  extends NioIndexWriter[ChunkIndex](path)
    with BasicIndexDataWriter
