package com.clemaire.cache.definitions

import java.io.Flushable

import com.clemaire.cache.definitions.chunk.{Chunk, ChunkBuilder, MutableChunk}
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
    * Converts a range of indexes into a range of chunks
    * and adds the new chunks to the [[Cache]] if possible.
    *
    * @param indexes The [[ChunkIndex]] values to get the
    *                chunks of.
    * @return The chunks represented by the given indexes.
    */
  override protected def getChunksByIndexes(indexes: Traversable[CI]): Traversable[Chunk[D]] = {
    super.getChunksByIndexes(indexes)
  }

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
    checkAndAdd(currentChunk)
    currentChunk = new MutableChunk[D](ci.id + 1)
  }

  /**
    * Appends a data entry to the existing [[Cache]]
    * and writes it into the current or a new chunk.
    *
    * @param data The data entry to add.
    * @return This [[Cache]].
    */
  def +=(data: D): this.type = {
    currentChunk += data
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
