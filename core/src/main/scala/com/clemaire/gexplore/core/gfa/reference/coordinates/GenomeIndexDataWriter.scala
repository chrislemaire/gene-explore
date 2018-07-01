package com.clemaire.gexplore.core.gfa.reference.coordinates

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.{DataWriter, LengthByType}

trait GenomeIndexDataWriter
  extends DataWriter[GenomeCoordinateChunkIndex]
    with LengthByType[GenomeCoordinateChunkIndex] {

  override protected[this] def length(obj: GenomeCoordinateChunkIndex): Int =
    4 + 8 + 8 + 4 + 4 + 4 + 4 + 12 * obj.referenceCoordinates.size

  override protected[this] def write(chunkIndex: GenomeCoordinateChunkIndex,
                                     os: DataOutputStream): Unit = {
    os.writeInt(chunkIndex.id)
    os.writeLong(chunkIndex.filePos)
    os.writeLong(chunkIndex.length)
    os.writeInt(chunkIndex.layers.getStart)
    os.writeInt(chunkIndex.layers.getEnd)
    os.writeInt(chunkIndex.segmentIds.getStart)
    os.writeInt(chunkIndex.segmentIds.getEnd)

    chunkIndex.referenceCoordinates.foreach(kv => {
      os.writeInt(kv._1)
      os.writeLong(kv._2)
    })
  }

  override protected[this] def write(chunkIndex: GenomeCoordinateChunkIndex,
                                     ob: ByteBuffer): Unit = {
    ob.putInt(chunkIndex.id)
    ob.putLong(chunkIndex.filePos)
    ob.putLong(chunkIndex.length)
    ob.putInt(chunkIndex.layers.getStart)
    ob.putInt(chunkIndex.layers.getEnd)
    ob.putInt(chunkIndex.segmentIds.getStart)
    ob.putInt(chunkIndex.segmentIds.getEnd)

    chunkIndex.referenceCoordinates.foreach(kv => {
      ob.putInt(kv._1)
      ob.putLong(kv._2)
    })
  }
}
