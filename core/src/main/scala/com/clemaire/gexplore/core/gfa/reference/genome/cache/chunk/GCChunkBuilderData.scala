package com.clemaire.gexplore.core.gfa.reference.genome.cache.chunk

import com.clemaire.cache.definitions.chunk.ChunkBuilderData

import scala.collection.mutable

trait GCChunkBuilderData
  extends ChunkBuilderData {

  /**
    * The current total coordinates. These are
    * updated each node by adding the length of
    * the node for each of its genomes.
    */
  protected[this] val currentCoordinates: mutable.HashMap[Int, Long] =
    mutable.HashMap()

  /**
    * The current relative coordinates. These are
    * updated only once when reset happens.
    */
  protected[this] val relativeCoordinates: mutable.HashMap[Int, Long] =
    mutable.HashMap()

}
