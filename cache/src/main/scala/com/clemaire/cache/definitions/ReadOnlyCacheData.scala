package com.clemaire.cache.definitions

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.index.{ChunkIndex, ReadOnlyIndex}

import scala.collection.mutable

trait ReadOnlyCacheData[D <: Identifiable, CI <: ChunkIndex] {

  /**
    * The index used to lookup the right
    * [[ChunkIndex]]es with.
    */
  val index: ReadOnlyIndex[CI]

  /**
    * The chunks loaded in-memory. Loaded chunks are
    * kept in a mapping from their (index-)ids to the
    * chunks themselves.
    */
  protected val loadedChunks: mutable.HashMap[Int, Chunk[D]] =
    mutable.HashMap()

}
