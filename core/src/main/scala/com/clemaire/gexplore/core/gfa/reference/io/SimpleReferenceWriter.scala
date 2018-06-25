package com.clemaire.gexplore.core.gfa.reference.io

import java.io.DataOutputStream
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.reference.{ReferenceNode, ReferenceWriter}

/**
  * [[ReferenceWriter]] defining functions to write
  * [[ReferenceNode]]s in the 'simple' format.
  *
  * The following lines describe this format with, on
  * the first line, an identifier of the data that is
  * written and, on the second line, the length of
  * that data in bytes.
  *
  * {{{
  * [ID][FILE_POS][C_LEN][OUT_LEN][IN_LEN][GC_LEN][OUT_EDGES ][IN_EDGES ][   GC    ]
  * [ 4][    8   ][  4  ][   4   ][  4   ][  4   ][12*OUT_LEN][12*IN_LEN][12*GC_LEN]
  * }}}
  */
trait SimpleReferenceWriter
  extends ReferenceWriter {

  /**
    * Calculates the length of a given [[ReferenceNode]]
    * when written to disk in bytes.
    *
    * @param node The [[ReferenceNode]] to calculate the
    *             length of.
    * @return The length of a written version of the given
    *         node in bytes.
    */
  def length(node: ReferenceNode): Int = {
    4 + 8 + 4 * 4 + 12 * (node.outgoingEdges.length +
      node.incomingEdges.length + node.genomeCoordinates.length)
  }

  /**
    * Writes a single node to the given [[DataOutputStream]]
    * in the format of a ''simple reference node''.
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

  /**
    * Writes a single node to the given [[ByteBuffer]]
    * in the format of a ''simple reference node''.
    *
    * @param node The [[ReferenceNode]] to write.
    * @param ob   The [[ByteBuffer]] to write to.
    */
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
