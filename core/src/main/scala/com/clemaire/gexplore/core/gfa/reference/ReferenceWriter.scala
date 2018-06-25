package com.clemaire.gexplore.core.gfa.reference

import java.io.Writer

trait ReferenceWriter extends Writer {

  override def write(cbuf: Array[Char], off: Int, len: Int): Unit = {}

  /**
    * Writes a single ReferenceNode to file.
    *
    * @param node The [[ReferenceNode]] to write
    *             to file.
    */
  def write(node: ReferenceNode): Unit

}
