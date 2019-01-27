package com.clemaire.gexplore.core.gfa.reference.writing.additional

import java.io.Writer

import com.clemaire.gexplore.core.gfa.reference.data.BuilderReferenceNode

abstract class AdditionalReferenceWriter
  extends Writer {

  /**
    * Writes the given node to the additional
    * reference cache through this writer.
    *
    * @param node       The [[BuilderReferenceNode]] to write.
    * @param byteLength The number of bytes written to
    *                   disk to store the [[BuilderReferenceNode]].
    */
  def writeNode(node: BuilderReferenceNode,
                byteLength: Int): Unit

}
