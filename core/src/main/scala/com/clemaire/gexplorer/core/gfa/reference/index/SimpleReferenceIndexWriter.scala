package com.clemaire.gexplorer.core.gfa.reference.index

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplorer.core.gfa.reference.additional.AdditionalReferenceWriter

trait SimpleReferenceIndexWriter
  extends AdditionalReferenceWriter {

  /**
    * The length of each chunk index in the written file.
    */
  protected[this] val chunkLength: Int = 4 * 6 + 8

  protected[this] def write(indexChunk: ReferenceChunkIndex,
                            os: DataOutputStream): Unit = {
    os.writeInt(indexChunk.id)
    os.writeLong(indexChunk.filePos)
    os.writeInt(indexChunk.length)
    os.writeInt(indexChunk.layers.getStart)
    os.writeInt(indexChunk.layers.getEnd)
    os.writeInt(indexChunk.segmentIds.getStart)
    os.writeInt(indexChunk.segmentIds.getEnd)
  }

  protected[this] def write(indexChunk: ReferenceChunkIndex,
                            ob: ByteBuffer): Unit = {
    ob.putInt(indexChunk.id)
    ob.putLong(indexChunk.filePos)
    ob.putInt(indexChunk.length)
    ob.putInt(indexChunk.layers.getStart)
    ob.putInt(indexChunk.layers.getEnd)
    ob.putInt(indexChunk.segmentIds.getStart)
    ob.putInt(indexChunk.segmentIds.getEnd)
  }

}
