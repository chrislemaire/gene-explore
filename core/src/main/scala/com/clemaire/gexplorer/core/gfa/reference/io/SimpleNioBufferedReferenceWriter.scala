package com.clemaire.gexplorer.core.gfa.reference.io

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}

class SimpleNioBufferedReferenceWriter(paths: CachePathList)
  extends ReferenceWriter
    with SimpleReferenceWriter {

  private val fc: FileChannel = FileChannel.open(paths.referenceFilePath,
    StandardOpenOption.WRITE, StandardOpenOption.CREATE)

  private val buffer: ByteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 8)

  override def write(node: ReferenceNode): Unit = {
    val len = length(node)
    if (buffer.position + len > buffer.capacity()) {
      flushBuffer()
    }

    write(node, buffer)
  }

  override def flush(): Unit =
    flushBuffer()

  def flushBuffer(): Unit = {
    buffer.flip()
    fc.write(buffer)
    buffer.clear()
  }

  override def close(): Unit =
    fc.close()
}
