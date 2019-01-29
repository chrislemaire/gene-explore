package com.clemaire.gexplore.core.gfa.reference.writing.coordinates.data

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.{DataWriter, LengthByType}

trait GCDataWriter
  extends DataWriter[(Int, Traversable[(Int, Long)])]
    with LengthByType[Traversable[(Int, Long)]] {

  override def length(genomeCoordinates: Traversable[(Int, Long)]): Int =
    4 + 4 + 8 * genomeCoordinates.size

  override protected[this] def write(obj: (Int, Traversable[(Int, Long)]),
                                     os: DataOutputStream): Unit = {
    os.writeInt(obj._2.size)
    os.writeInt(obj._1)

    obj._2.foreach(kv => {
      os.writeInt(kv._1)
      os.writeInt(kv._2.toInt)
    })
  }

  override protected[this] def write(obj: (Int, Traversable[(Int, Long)]),
                                     ob: ByteBuffer): Unit = {
    ob.putInt(obj._2.size)
    ob.putInt(obj._1)

    obj._2.foreach(kv => {
      ob.putInt(kv._1)
      ob.putInt(kv._2.toInt)
    })
  }
}
