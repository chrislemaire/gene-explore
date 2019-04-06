package com.clemaire.gexplore.core.gfa.cache.io

import java.nio.file.Path

import com.clemaire.cache.impl.io.writing.NioIndexWriter
import com.clemaire.gexplore.core.gfa.cache.index.PositionalChunkIndex

class PositionalIndexWriter(override val path: Path)
  extends NioIndexWriter[PositionalChunkIndex](path)
    with PositionalIndexDataWriter
