package com.clemaire.cache.definitions

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.index.{ChunkIndex, ReadOnlyIndex}

import scala.collection.mutable
import scala.reflect.ClassTag

trait ReadOnlyCacheData[D <: Identifiable, CI <: ChunkIndex] {

  /**
    * Implicit class tag for D.
    */
  protected implicit val D: ClassTag[D] = implicitly[ClassTag[D]]

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
