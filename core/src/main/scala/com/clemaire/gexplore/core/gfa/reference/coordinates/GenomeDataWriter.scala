package com.clemaire.gexplore.core.gfa.reference.coordinates

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.{DataWriter, LengthByType}

trait GenomeDataWriter
  extends DataWriter[(Int, Map[Int, Long])]
    with LengthByType[Map[Int, Long]] {

  override def length(genomeCoordinates: Map[Int, Long]): Int =
    4 + 4 + 8 * genomeCoordinates.size

  override protected[this] def write(obj: (Int, Map[Int, Long]),
                                     os: DataOutputStream): Unit = {
    os.writeInt(obj._1)

    obj._2.foreach(kv => {
      os.writeInt(kv._1)
      os.writeInt(kv._2.toInt)
    })
  }

  override protected[this] def write(obj: (Int, Map[Int, Long]),
                                     ob: ByteBuffer): Unit = {
    ob.putInt(obj._1)

    obj._2.foreach(kv => {
      ob.putInt(kv._1)
      ob.putInt(kv._2.toInt)
    })
  }
}
