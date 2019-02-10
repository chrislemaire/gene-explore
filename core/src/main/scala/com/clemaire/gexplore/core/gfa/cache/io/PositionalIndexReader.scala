package com.clemaire.gexplore.core.gfa.cache.io

import java.nio.file.Path

import com.clemaire.cache.impl.io.reading.NioIndexReader
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex

class PositionalIndexReader(override val path: Path)
  extends NioIndexReader[PositionalChunkIndex](path)
    with PositionalIndexDataReader
