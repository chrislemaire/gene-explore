package com.clemaire.cache.impl.io.reading

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ChunkIndex

class BasicIndexReader(path: Path)
  extends NioIndexReader[ChunkIndex](path)
    with BasicIndexDataReader
