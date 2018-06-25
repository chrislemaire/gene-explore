package com.clemaire.gexplore.core.gfa.reference.additional

import java.io.Writer

import com.clemaire.gexplore.core.gfa.reference.ReferenceNode

abstract class AdditionalReferenceWriter
  extends Writer {

  /**
    * Writes the given node to the additional
    * reference cache through this writer.
    *
    * @param node The [[ReferenceNode]] to write.
    */
  def writeNode(node: ReferenceNode): Unit

  override def write(cbuf: Array[Char],
                     off: Int,
                     len: Int): Unit = {}

}
