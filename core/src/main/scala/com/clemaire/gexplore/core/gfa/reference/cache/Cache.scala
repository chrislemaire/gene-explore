package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.reference.cache.cache.capacity.CapacityLimiter
import com.clemaire.gexplore.core.gfa.reference.cache.cache.scheduling.CacheScheduler
import com.clemaire.gexplore.core.gfa.reference.index.{AbstractIndex, ChunkIndex}

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
trait Cache[I <: AbstractIndex[CI], CI <: ChunkIndex, D]
  extends Object
    with CacheData[Chunk[CI, D]]
    with CapacityLimiter
    with CacheScheduler[Chunk[CI, D]] {

  val index: I

  /**
    * Calls the appropriate functions to fetch chunks from
    * disk and returns them as a mapping of their respective
    * chunk ids mapped to the chunks themselves.
    *
    * @param chunks The indexes of the chunks to load into memory.
    * @return A mapping of chunk ids to chunks.
    */
  protected def fetch(chunks: Set[CI]): Map[Int, Chunk[CI, D]]

  /**
    * Loads the requested chunks into memory if they weren't
    * already fetched and returns a mapping of chunk ids to
    * chunks containing all requested chunks.
    *
    * @param chunksToLoad The indexes of the chunks to load.
    * @return A mapping of chunk ids to chunks.
    */
  def load(chunksToLoad: Set[CI]): Map[Int, Chunk[CI, D]] = {
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
