package com.clemaire.gexplore.core.gfa.coordinates.dummy.cache.dummy

import java.nio.file.Path

import com.clemaire.cache.impl.io.writing.NioChunkWriter
import com.clemaire.cache.impl.io.InstanceLength
import com.clemaire.gexplore.core.gfa.coordinates.dummy.DummyNode
import com.clemaire.io.fixture.OutputFixture

class DummyNodeWriter(path: Path)
  extends NioChunkWriter[DummyNode](path)
    with InstanceLength[DummyNode] {
  
  /**
    * Sets the object to find the length of and
    * subsequently prepares the length variable
    * with the calculated length for the object.
    *
    * @param t The object to calculate the length
    *          for.
    */
  override def forObj(t: DummyNode): Unit =
    if (!t.isSegment) _length = 4 * 4 + 4
    else _length = 4 * 4 + 4 + 4

  /**
    * Writes the given data object of type [[DummyNode]]
    * to the given source represented as an
    * [[OutputFixture]].
    *
    * @param data The data object to write.
    * @param out  The source to write to represented
    *             as an [[OutputFixture]].
    */
  override def writeData(data: DummyNode, out: OutputFixture): Unit = {
    out.writeInt(if (!data.isSegment) 0 else 1)
    out.writeInt(data.id)
    out.writeInt(data.outgoing.head)
    out.writeInt(data.incoming.head)

    if (!data.isSegment) {
      out.writeInt(data.layer)
    } else {
      out.writeInt(data.layerStart)
      out.writeInt(data.layerEnd)
    }
  }

}
