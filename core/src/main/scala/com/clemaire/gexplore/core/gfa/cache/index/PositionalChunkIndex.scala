package com.clemaire.gexplore.core.gfa.cache.index

import com.clemaire.cache.definitions.index.ChunkIndex
import com.lodborg.intervaltree.IntegerInterval

trait PositionalChunkIndex
  extends ChunkIndex {

  /**
    * The layers of the data objects represented
    * by this [[PositionalChunkIndex]].
    */
  val layers: IntegerInterval
}

object PositionalChunkIndex {
  private case class BasicPositionalChunkIndex
  (id: Int,
   filePosition: Long,
   length: Long,
   ids: IntegerInterval,
   layers: IntegerInterval)
    extends PositionalChunkIndex

  /**
    * Constructs a [[PositionalChunkIndex]] from the given
    * [[ChunkIndex]] information and layer interval.
    *
    * @param id           The id of the chunk.
    * @param filePosition The file position at which data
    *                     of the chunk starts.
    * @param length       The length of the chunk in data.
    * @param layers       The range of layers covered by
    *                     the data for this chunk.
    * @param ids          The range of ids covered by the
    *                     data for this chunk.
    * @return A [[PositionalChunkIndex]] representing the
    *         given information.
    */
  def apply(id: Int,
            filePosition: Long,
            length: Long,
            ids: IntegerInterval,
            layers: IntegerInterval): PositionalChunkIndex =
    BasicPositionalChunkIndex(id, filePosition, length, ids, layers)

}
