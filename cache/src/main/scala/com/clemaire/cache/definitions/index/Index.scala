package com.clemaire.cache.definitions.index

import java.io.Flushable

import com.clemaire.cache.definitions.io.writing.IndexWriter
import com.clemaire.interval.IntervalTreeMap

trait Index[CI <: ChunkIndex]
  extends IntervalTreeMap[Integer, CI]
    with ReadOnlyIndex[CI]
    with AutoCloseable
    with Flushable {

  override protected[this] val index: IntervalTreeMap[Integer, CI] = this

  /**
    * The [[IndexWriter]] using which every added
    * [[ChunkIndex]] will be written to the output
    * source.
    */
  val writer: IndexWriter[CI]

  /**
    * Appends a given [[ChunkIndex]] to this [[Index]].
    * If needed, it also writes the added [[ChunkIndex]]
    * to disk using the supplied [[IndexWriter]]
    *
    * @param ci The [[ChunkIndex]] to add.
    */
  def +=(ci: CI): Unit = {
    super.addBinding(ci.ids, ci)
    writer.write(ci)
  }

  /**
    * Constructs and returns a safe immutable
    * [[ReadOnlyIndex]] for further use.
    *
    * @return Immutable [[ReadOnlyIndex]] for further use.
    */
  def readOnly: ReadOnlyIndex[CI]

  def flush(): Unit = {
    writer.flush()
  }

  def close(): Unit = {
    writer.close()
  }

}
