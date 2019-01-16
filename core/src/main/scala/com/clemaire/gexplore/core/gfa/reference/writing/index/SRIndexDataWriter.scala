package com.clemaire.gexplore.core.gfa.reference.writing.index

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.{DataWriter, StaticLength}
import com.clemaire.gexplore.core.gfa.reference.index.ReferenceChunkIndex

trait SRIndexDataWriter
  extends DataWriter[ReferenceChunkIndex]
    with StaticLength {

  override val LENGTH: Int = 4 * 6 + 8

  override def write(indexChunk: ReferenceChunkIndex,
                     os: DataOutputStream): Unit = {
    os.writeInt(indexChunk.id)
    os.writeLong(indexChunk.filePos)
    os.writeInt(indexChunk.length)
    os.writeInt(indexChunk.layers.getStart)
    os.writeInt(indexChunk.layers.getEnd)
    os.writeInt(indexChunk.segmentIds.getStart)
    os.writeInt(indexChunk.segmentIds.getEnd)
  }

  override def write(indexChunk: ReferenceChunkIndex,
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
