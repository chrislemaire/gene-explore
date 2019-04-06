package com.clemaire.gexplore.core.gfa.reference.genome.cache

import java.nio.file.Path

import com.clemaire.cache.definitions.index.ReadOnlyIndex
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.ReadOnlyCache
import com.clemaire.cache.impl.capacity.SetNumberOfChunks
import com.clemaire.cache.impl.scheduling.LRU
import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.genome.cache.index.GCChunkIndex
import com.clemaire.gexplore.core.gfa.reference.genome.cache.reading.{GCIndexReader, GCReader}

import scala.reflect.ClassTag

class GCReadOnlyCache
(val reader: ChunkReader[GenomeCoordinate, GCChunkIndex],
 val index: ReadOnlyIndex[GCChunkIndex],
 val max: Int = 25)
(override protected implicit val D: ClassTag[GenomeCoordinate])
  extends ReadOnlyCache[GenomeCoordinate, GCChunkIndex]
    with SetNumberOfChunks[GenomeCoordinate, GCChunkIndex]
    with LRU[GenomeCoordinate, GCChunkIndex] {

  def this(reader: ChunkReader[GenomeCoordinate, GCChunkIndex], indexPath: Path, n: Int) =
    this(reader, new GCIndexReader(indexPath, n).read, n)

  def this(dataPath: Path, indexPath: Path, n: Int) =
    this(new GCReader(dataPath, n), indexPath, n)

}
