package com.clemaire.gexplore.core.gfa.cache

import com.clemaire.gexplore.core.gfa.cache.capacity.CapacityLimiter
import com.clemaire.gexplore.core.gfa.cache.scheduling.CacheScheduler
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
  * @tparam C The type of chunk stored in the cache.
  * @tparam CI The type of index used to fetch chunks.
  */
abstract class Cache[C <: Chunk[CI, _], CI <: ChunkIndex]
  extends Object
    with CacheData[C]
    with CapacityLimiter
    with CacheScheduler[C]
    with ChunkFetcher[C, CI] {

  val index: AbstractIndex[CI]

  /**
    * Loads the requested chunks into memory if they weren't
    * already fetched and returns a mapping of chunk ids to
    * chunks containing all requested chunks.
    *
    * @param chunksToLoad The indexes of the chunks to load.
    * @return A mapping of chunk ids to chunks.
    */
  def load(chunksToLoad: Set[CI]): Map[Int, C] = {
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
