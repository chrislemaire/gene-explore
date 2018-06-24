package com.clemaire.gexplorer.core.gfa.reference.io

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}

/**
  * [[ReferenceWriter]] defining functions to write
  * [[ReferenceNode]]s in the 'simple' format.
  */
trait SimpleReferenceWriter extends ReferenceWriter {

  def length(node: ReferenceNode): Int = {
    4 + 8 + 16 + 12 * (node.outgoingEdges.length +
      node.incomingEdges.length + node.genomeCoordinates.length)
  }

  /**
    * Writes a single node to the given [[DataOutputStream]].
    *
    * @param node The [[ReferenceNode]] to write.
    * @param os   The [[DataOutputStream]] to write to.
    */
  def write(node: ReferenceNode,
            os: DataOutputStream): Unit = {
    os.writeInt(node.id)
    os.writeLong(node.fileOffset)
    os.writeInt(node.contentLength)
    os.writeInt(node.outgoingEdges.length)
    os.writeInt(node.incomingEdges.length)
    os.writeInt(node.genomeCoordinates.length)

    node.outgoingEdges.foreach(kv => {
      os.writeInt(kv._1)
      os.writeLong(kv._2)
    })
    node.incomingEdges.foreach(kv => {
      os.writeInt(kv._1)
      os.writeLong(kv._2)
    })

    node.genomeCoordinates.foreach(kv => {
      os.writeInt(kv._1)
      os.writeLong(kv._2)
    })
  }

  def write(node: ReferenceNode,
            ob: ByteBuffer): Unit = {
    ob.putInt(node.id)
    ob.putLong(node.fileOffset)
    ob.putInt(node.contentLength)
    ob.putInt(node.outgoingEdges.length)
    ob.putInt(node.incomingEdges.length)
    ob.putInt(node.genomeCoordinates.length)

    node.outgoingEdges.foreach(kv => {
      ob.putInt(kv._1)
      ob.putLong(kv._2)
    })
    node.incomingEdges.foreach(kv => {
      ob.putInt(kv._1)
      ob.putLong(kv._2)
    })

    node.genomeCoordinates.foreach(kv => {
      ob.putInt(kv._1)
      ob.putLong(kv._2)
    })
  }

}
