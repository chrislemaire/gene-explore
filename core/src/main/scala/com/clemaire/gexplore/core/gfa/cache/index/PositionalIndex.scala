package com.clemaire.gexplore.core.gfa.cache.index

import java.nio.file.Path

import com.clemaire.cache.definitions.index.{Index, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.gexplore.core.gfa.cache.io.PositionalIndexWriter
import com.clemaire.interval.IntervalTreeMap

class PositionalIndex[PCI <: PositionalChunkIndex]
(val writer: IndexWriter[PCI])
  extends Index[PCI]
    with PositionalReadOnlyIndex[PCI] {

  /**
    * The interval index mapping layer ranges to their
    * respective [[PositionalChunkIndex]]es.
    */
  val layerIndex: IntervalTreeMap[Integer, PCI] =
    new IntervalTreeMap()

  /**
    * Appends a given [[PositionalChunkIndex]] to this [[Index]].
    * If needed, it also writes the added [[PositionalChunkIndex]]
    * to disk using the supplied [[IndexWriter]]
    *
    * @param pci The [[PositionalChunkIndex]] to add.
    */
  override def +=(pci: PCI): Unit = {
    layerIndex.addBinding(pci.layers, pci)
    super.+=(pci)
  }

  /**
    * Constructs and returns a safe immutable
    * [[ReadOnlyIndex]] for further use.
    *
    * @return Immutable [[ReadOnlyIndex]] for further use.
    */
  def readOnly: PositionalReadOnlyIndex[PCI] =
    PositionalReadOnlyIndex(this)

}

object PositionalIndex {
  def apply(indexPath: Path): PositionalIndex[PositionalChunkIndex] =
    new PositionalIndex[PositionalChunkIndex](
      new PositionalIndexWriter(indexPath))
}
