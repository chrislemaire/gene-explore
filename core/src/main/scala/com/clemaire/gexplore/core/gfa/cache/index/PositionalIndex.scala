package com.clemaire.gexplore.core.gfa.cache.index

import com.clemaire.cache.definitions.index.{Index, ReadOnlyIndex}
import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.interval.IntervalTreeMap

class PositionalIndex[PCI <: PositionalChunkIndex]
(val writer: IndexWriter[PCI])
  extends PositionalReadOnlyIndex[PCI](this)
    with Index[PCI] {

  /**
    * The interval index mapping layer ranges to their
    * respective [[PositionalChunkIndex]]es.
    */
  val layerIndex: IntervalTreeMap[Integer, PCI] =
    new IntervalTreeMap()

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
  def readOnly: ReadOnlyIndex[PCI] =
    PositionalReadOnlyIndex(this)

}
