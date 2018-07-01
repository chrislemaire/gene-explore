package com.clemaire.gexplore.core.gfa.reference.writing.io

import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.writing.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.core.gfa.reference.writing.ReferenceNodeWriter

trait AdditionalReferenceWriterWorkBuffer
  extends ReferenceNodeWriter {

  /**
    * The sequence of [[AdditionalReferenceWriter]]
    * containing all writers that are to be called for
    * each processed node in addition to the reference
    * cache writer.
    */
  protected[this] val additionalWriters: Seq[AdditionalReferenceWriter] =
    Seq.empty

  protected[this] def acceptWork(work: (ReferenceNode, Int)): Unit = {
    additionalWriters.foreach(_.writeNode(work._1, work._2))
  }

}
