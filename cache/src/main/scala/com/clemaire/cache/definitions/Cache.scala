package com.clemaire.cache.definitions

import java.io.Flushable

import com.clemaire.cache.definitions.chunk.ChunkBuilder
import com.clemaire.cache.definitions.index.{ChunkIndex, Index}
import com.clemaire.cache.definitions.io.writing.ChunkWriter

trait Cache[D <: Identifiable, CI <: ChunkIndex]
  extends ReadOnlyCache[D, CI]
    with Flushable {

  /**
    * The index used to lookup the required
    * [[ChunkIndex]]es with and write new [[ChunkIndex]]
    * values to.
    */
  val index: Index[CI]

  /**
    * The [[ChunkWriter]] used to write data entries
    * to the underlying source with.
    */
  val writer: ChunkWriter[D]

  /**
    * The [[ChunkBuilder]] that governs deciding on
    * when a [[com.clemaire.cache.definitions.chunk.Chunk]]
    * is complete and constructs it when it is ready.
    */
  val chunkBuilder: ChunkBuilder[D, CI]

  /**
    * Adds the currently building [[com.clemaire.cache.definitions.chunk.Chunk]]
    * to the index by its constructs [[ChunkIndex]] and to
    * the current in-memory [[Cache]].
    *
    * @param ci The [[ChunkIndex]] describing the current
    *           [[com.clemaire.cache.definitions.chunk.Chunk]].
    */
  protected[this] def addCurrentChunk(ci: CI): Unit = {
    index += ci
    checkAndAdd(reader.constructChunk(ci.id, currentChunk.toMap))
    currentChunk.clear()
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
  override def getRange(left: Int, right: Int): Map[Int, D] =
    super.getRange(left, right) ++ currentChunk
      .filter(data => left <= data._1 && data._1 <= right)

  /**
    * Appends a data entry to the existing [[Cache]]
    * and writes it into the current or a new chunk.
    *
    * @param data The data entry to add.
    * @return This [[Cache]].
    */
  def +=(data: D): this.type = {
    currentChunk += data.id -> data
    writer.write(data)

    chunkBuilder.register(data, writer.length)
      .foreach(addCurrentChunk)

    this
  }

  /**
    * Appends all given data entries one-by-one to the
    * existing [[Cache]] and writes them into the current
    * or a new chunk.
    *
    * @param data The data entries to add.
    * @return This [[Cache]].
    */
  def ++=(data: Traversable[D]): this.type = {
    data.foreach(+=)
    this
  }

  override def flush(): Unit = {
    chunkBuilder.finish.foreach(addCurrentChunk)

    index.flush()
    writer.flush()
  }

  override def close(): Unit = {
    index.close()
    writer.close()
    reader.close()
  }

  /**
    * Constructs a read-only [[Cache]] from this
    * [[Cache]].
    *
    * @return The constructed [[ReadOnlyCache]].
    */
  def readOnly: ReadOnlyCache[D, CI]

  /**
    * Finishes the current [[Cache]] writing operations
    * and constructs a [[ReadOnlyCache]] to be further
    * used without risk of accidental mutation.
    *
    * @return The [[ReadOnlyCache]] safe to use without
    *         mutation.
    */
  def finish: ReadOnlyCache[D, CI] = {
    flush()

    index.close()
    writer.close()

    readOnly
  }

}
