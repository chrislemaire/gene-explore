package com.clemaire.gexplorer.core.gfa.reference.io

import com.clemaire.gexplorer.core.gfa.reference.{ReferenceNode, ReferenceWriter}
import com.clemaire.gexplorer.core.gfa.reference.additional.AdditionalReferenceWriter

trait AdditionalReferenceWriters
  extends ReferenceWriter {

  /**
    * The sequence of [[AdditionalReferenceWriter]]
    * containing all writers that are to be called for
    * each processed node in addition to the reference
    * cache writer.
    */
  protected[this] val additionalWriters: Seq[AdditionalReferenceWriter] =
    Seq.empty

  protected[this] def acceptWork(work: ReferenceNode): Unit = {
    additionalWriters.foreach(_.writeNode(work))
  }

}
