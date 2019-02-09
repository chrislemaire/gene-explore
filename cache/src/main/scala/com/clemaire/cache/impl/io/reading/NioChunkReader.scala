package com.clemaire.cache.impl.io.reading

import java.nio.channels.FileChannel
import java.nio.file.{Path, StandardOpenOption}
import java.nio.ByteBuffer

import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.ChunkReader
import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.chunk.Chunk
import com.clemaire.io.fixture.ByteBufferFixture.bb2fixtureIn

import scala.collection.mutable

abstract class NioChunkReader[D <: Identifiable, CI <: ChunkIndex](val path: Path)
  extends ChunkReader[D, CI] {

  /**
    * The [[FileChannel]] used to read input from.
    */
  private[this] val fc: FileChannel = FileChannel.open(path,
    StandardOpenOption.READ)

  /**
    * Constructs a [[Chunk]] from its id and data.
    *
    * @param id   The unique identifier for the [[Chunk]].
    * @param data The data passed to the [[Chunk]].
    * @return The constructed [[Chunk]].
    */
  def constructChunk(id: Int, data: Map[Int, D]): Chunk[D] =
    Chunk(id, data)

  /**
    * Reads a single chunk from the input source starting
    * at the file position and ending at the file position +
    * length in the given [[ChunkIndex]].
    *
    * @param index The [[ChunkIndex]] to read the chunk of.
    * @return The read [[Chunk]].
    */
  override def read(index: CI): Chunk[D] = {
    val buffer = ByteBuffer.allocateDirect(index.length.toInt)
    fc.read(buffer, index.filePosition)
    buffer.flip()

    val data = mutable.HashMap[Int, D]()
    while (buffer.hasRemaining) {
      val d = readData(buffer)
      data += d.id -> d
    }

    constructChunk(index.id, data.toMap)
  }

  override def close(): Unit =
    fc.close()

}
