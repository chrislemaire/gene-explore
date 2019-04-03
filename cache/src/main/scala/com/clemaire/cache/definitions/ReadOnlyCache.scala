package com.clemaire.cache.definitions

import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.scheduling.CacheScheduler
import metal.mutable.HashMap
import metal.syntax._

trait ReadOnlyCache[D <: Identifiable, CI <: ChunkIndex]
  extends CacheData[D, CI]
    with ReadOnlyCacheData[D, CI]
    with CapacityLimiter
    with CacheScheduler[D, CI]
    with AutoCloseable {

  /**
    * The [[ChunkReader]] used to read chunks of data
    * entries from the data file.
    */
  protected[this] val reader: ChunkReader[D, CI]

  /**
    * Adds a given chunk to the chunk list after first
    * checking whether the current cache is full.
    *
    * If the [[Cache]] is currently full, items are removed
    * until there is space for the new [[Chunk]].
    *
    * @param c The [[Chunk]] to add.
    */
  protected[this] def checkAndAdd(c: Chunk[D]): Unit = {
    while (full) removeNext()
    add(c)
  }

  /**
    * Converts a range of indexes into a range of chunks
    * and adds the new chunks to the [[Cache]] if possible.
    *
    * @param indexes The [[ChunkIndex]] values to get the
    *                chunks of.
    * @return The chunks represented by the given indexes.
    */
  protected[this] def getChunksByIndexes(indexes: Traversable[CI]): Traversable[Chunk[D]] = {
    val loaded = indexes
      .map(ic => loadedChunks.get(ic.id))
      .filter(_.isDefined)
      .map(_.get)

    val read = reader.read(indexes.filterNot(ic =>
      loadedChunks.contains(ic.id)))

    read.foreach(checkAndAdd)

    loaded ++ read
  }

  /**
    * @return The highest id in this Cache.
    */
  def maxId: Int = index.maxId

  /**
    * Gets a data entry by its id from the underlying
    * Cache implementation.
    *
    * @param id The identifier to lookup a data entry
    *           from.
    * @return An [[Option]] containing the data entry
    *         if one exists with the given id.
    */
  def get(id: Int): Option[D] =
    Option(getRange(id, id)).filter(_.contains(id)).map(_(id))

  /**
    * Gets data entries by their ids from the underlying
    * Cache implementation.
    *
    * @param ids The identifiers to lookup data entries
    *            from.
    * @return A [[Map]] containing all existing data entries
    *         mapped by their id with an id in the given set.
    */
  def get(ids: Set[Int]): HashMap[Int, D] = {
    val chunks = getChunksByIndexes(index.get(ids))
    val result = HashMap[Int, D]()

    chunks.foreach(c => {
      c.data.foreach((id, d) => {
        if (ids(id))
          result.update(id, d)
      })
    })

    result
  }

  /**
    * Gets a range of data entries by their ids from
    * the underlying Cache implementation.
    *
    * The given range is closed and as such will include
    * both extremes.
    *
    * @param left  The left-bound for the range of data
    *              entry ids to fetch.
    * @param right The right-bound for the range of data
    *              entry ids to fetch.
    * @return A [[Map]] containing all existing data entries
    *         mapped by their id with an id in the given range.
    */
  def getRange(left: Int, right: Int): HashMap[Int, D] = {
    val chunks = getChunksByIndexes(index.getRange(left, right))
    val result = HashMap[Int, D]()

    chunks.foreach(c => {
      c.data.foreach((id, d) => {
        if (left <= id && id <= right)
          result.update(id, d)
      })
    })

    result
  }

  override def close(): Unit = {
    reader.close()
  }

}
