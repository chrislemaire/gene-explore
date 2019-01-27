package com.clemaire.gexplore.core.gfa.reference.writing.additional

import java.io.Writer

import com.clemaire.gexplore.core.gfa.reference.data.ReferenceNode

abstract class AdditionalReferenceWriter
  extends Writer {

  /**
    * Writes the given node to the additional
    * reference cache through this writer.
    *
    * @param node       The [[ReferenceNode]] to write.
    * @param byteLength The number of bytes written to
    *                   disk to store the [[ReferenceNode]].
    */
  def writeNode(node: ReferenceNode,
                byteLength: Int): Unit

}
