package com.clemaire.cache.impl

import java.nio.file.Path

import com.clemaire.cache.definitions.{Identifiable, ReadOnlyCache}
import com.clemaire.cache.definitions.index.{ChunkIndex, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.io.reading.{BasicIndexDataReader, NioIndexReader}
import com.clemaire.cache.impl.scheduling.LRU

import scala.reflect.ClassTag

class BasicReadOnlyCache[D <: Identifiable]
(val reader: ChunkReader[D, ChunkIndex],
 val index: ReadOnlyIndex[ChunkIndex],
 val max: Int = 25)
(implicit D: ClassTag[D])
  extends ReadOnlyCache[D, ChunkIndex]
    with SetNumberOfChunks[D, ChunkIndex]
    with LRU[D, ChunkIndex] {

  def this(reader: ChunkReader[D, ChunkIndex],
           indexPath: Path)
          (implicit D: ClassTag[D]) =
    this(reader, (new NioIndexReader[ChunkIndex](indexPath)
      with BasicIndexDataReader).read)

}
