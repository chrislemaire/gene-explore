package com.clemaire.cache.definitions.index

import com.clemaire.cache.definitions.Identifiable
import com.lodborg.intervaltree.IntegerInterval

trait ChunkIndex
  extends Identifiable {

  val id: Int
  val filePosition: Long
  val length: Long

  val ids: IntegerInterval
}

object ChunkIndex {
  private case class BasicChunkIndex
  (id: Int,
   filePosition: Long,
   length: Long,
   ids: IntegerInterval) extends ChunkIndex

  /**
    * Constructs a [[ChunkIndex]] from the given information.
    *
    * @param id           The id of the chunk.
    * @param filePosition The file position at which data
    *                     of the chunk starts.
    * @param length       The length of the chunk in data.
    * @param ids          The range of ids covered by the
    *                     data for this chunk.
    * @return A [[ChunkIndex]] object representing the
    *         given information.
    */
  def apply(id: Int,
            filePosition: Long,
            length: Long,
            ids: IntegerInterval): ChunkIndex =
    BasicChunkIndex(id, filePosition, length, ids)

}
