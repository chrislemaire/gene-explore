package com.clemaire.gexplore.core.gfa.reference.reading.coordinates.data

import com.clemaire.gexplore.core.gfa.reference.index.GCChunkIndex
import com.clemaire.gexplore.core.gfa.NioDataReader
import com.clemaire.gexplore.util.io.NioBufferedDataReader
import com.lodborg.intervaltree.IntegerInterval
import com.lodborg.intervaltree.Interval.Bounded

trait GCIndexDataReader
  extends NioDataReader[GCChunkIndex] {

  /**
    * The number of genomes to read.
    */
  private[this] var _n: Int = -1

  /**
    * Sets the number of genomes included in the index
    * to be read with this reader.
    *
    * @param n Number of genomes.
    */
  protected def withNGenomes(n: Int): this.type = {
    _n = n
    this
  }

  override def read(br: NioBufferedDataReader)
  : GCChunkIndex = GCChunkIndex(
    br.readInt,
    br.readLong,
    br.readInt,
    new IntegerInterval(br.readInt, br.readInt, Bounded.CLOSED),
    new IntegerInterval(br.readInt, br.readInt, Bounded.CLOSED),
    (1 to _n).map(_ => (br.readInt, br.readLong)).toMap
  )

}
