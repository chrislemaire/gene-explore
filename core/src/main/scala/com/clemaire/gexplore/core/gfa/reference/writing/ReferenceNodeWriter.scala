package com.clemaire.gexplore.core.gfa.reference.writing

import java.io.Writer

import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.index.{GenomeCoordinateIndex, ReferenceIndex}

trait ReferenceNodeWriter
  extends Writer {

  override def write(charBuf: Array[Char], off: Int, len: Int): Unit = {}

  /**
    * Writes a single ReferenceNode to file.
    *
    * @param node The [[ReferenceNode]] to write
    *             to file.
    */
  def write(node: ReferenceNode): Unit

  /**
    * @return The built [[ReferenceIndex]] resulting
    *         from writing all [[ReferenceNode]]s to disk.
    */
  def index: ReferenceIndex

  /**
    * @return The built [[GenomeCoordinateIndex]] resulting
    *         from writing all genome coordinates to disk.
    */
  def coordinatesIndex: GenomeCoordinateIndex

}
