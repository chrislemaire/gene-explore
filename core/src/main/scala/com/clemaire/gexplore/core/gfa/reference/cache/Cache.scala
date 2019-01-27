package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

import scala.collection.mutable

/**
  * Keeps track of some fragmented data. Data is
  * fragmented in chunks of which layer numbers and
  * segment ids are stored in an index.
  *
  * Caches have the responsibility of abstracting the
  * process of fetching missing chunks by simply delivering
  * any requested item regardless of whether it was previously
  * loaded.
  *
  * @tparam I The type of [[AbstractIndex]] used.
  * @tparam D The type of data returned upon retrieval calls.
  */
trait Cache[I <: AbstractIndex[CI], D, CI <: ChunkIndex] {

  val index: I

  protected val loadedChunks: mutable.HashMap[Int, Chunk[I, D]] =
    mutable.HashMap()

  /**
    * Decides whether this cache has reached its
    * maximum capacity and returns whether this is
    * the case.
    *
    * @return True when maximum cache capacity is reached
    *         false otherwise.
    */
  protected def reachedMax(): Boolean

  /**
    * Adds a chunk to the loaded chunks map and performs
    * any necessary cache scheduling activities.
    *
    * @param id    The index of the chunk to add.
    * @param chunk The chunk to add itself.
    */
  protected def add(id: Int,
                    chunk: Chunk[I, D]): Unit =
    loadedChunks(id) = chunk

  /**
    * Decides the next chunk to unload and removes it
    * from the loadedChunk map.
    */
  protected def removeNext(): Unit

  /**
    * Calls the appropriate functions to fetch chunks from
    * disk and returns them as a mapping of their respective
    * chunk ids mapped to the chunks themselves.
    *
    * @param chunks The indexes of the chunks to load into memory.
    * @return A mapping of chunk ids to chunks.
    */
  protected def fetch(chunks: Set[CI]): Map[Int, Chunk[I, D]]

  /**
    * Loads the requested chunks into memory if they weren't
    * already fetched and returns a mapping of chunk ids to
    * chunks containing all requested chunks.
    *
    * @param chunksToLoad The indexes of the chunks to load.
    * @return A mapping of chunk ids to chunks.
    */
  def load(chunksToLoad: Set[CI]): Map[Int, Chunk[I, D]] = {
    val chunksAlreadyFetched = chunksToLoad
      .filter(ci => loadedChunks.contains(ci.id))
      .map(ci => ci.id -> loadedChunks(ci.id))
      .toMap

    val fetchedChunks = fetch(chunksToLoad
      .filterNot(ci => loadedChunks.contains(ci.id)))
    fetchedChunks.foreach(t => {
      while (reachedMax()) removeNext()
      add(t._1, t._2)
    })

    chunksAlreadyFetched ++ fetchedChunks
  }

}
