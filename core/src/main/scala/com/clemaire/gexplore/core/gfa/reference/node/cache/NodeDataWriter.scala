package com.clemaire.gexplore.core.gfa.reference.node.cache

import com.clemaire.cache.definitions.io.writing.DataWriter
import com.clemaire.cache.impl.io.InstanceLength
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode
import com.clemaire.io.fixture.OutputFixture

trait NodeDataWriter
  extends DataWriter[ReferenceNode]
    with InstanceLength[ReferenceNode] {

  /**
    * Sets the object to find the length of and
    * subsequently prepares the length variable
    * with the calculated length for the object.
    *
    * @param t The object to calculate the length
    *          for.
    */
  override def forObj(t: ReferenceNode): Unit =
    _length = 4 + 4 + 4 + 8 + 4 + 12 * (t.outgoingEdges.size + t.incomingEdges.size)

  /**
    * Writes the given data object of type [[ReferenceNode]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: ReferenceNode, out: OutputFixture): Unit = {
    out.writeInt(data.outgoingEdges.size)
    out.writeInt(data.incomingEdges.size)

    out.writeInt(data.id)
    out.writeLong(data.fileOffset)
    out.writeInt(data.contentLength)

    data.outgoingEdges.foreach(kv => {
      out.writeInt(kv._1)
      out.writeLong(kv._2)
    })
    data.incomingEdges.foreach(kv => {
      out.writeInt(kv._1)
      out.writeLong(kv._2)
    })
  }
}
