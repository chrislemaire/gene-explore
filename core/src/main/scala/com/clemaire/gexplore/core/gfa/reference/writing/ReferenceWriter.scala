package com.clemaire.gexplore.core.gfa.reference.writing

import java.io.Writer

import com.clemaire.gexplore.core.gfa.reference.ReferenceNode

trait ReferenceWriter
  extends Writer {

  override def write(charBuf: Array[Char], off: Int, len: Int): Unit = {}

  /**
    * Writes a single ReferenceNode to file.
    *
    * @param node The [[ReferenceNode]] to write
    *             to file.
    */
  def write(node: ReferenceNode): Unit

}
