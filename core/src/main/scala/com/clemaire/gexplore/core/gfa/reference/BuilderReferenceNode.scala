package com.clemaire.gexplore.core.gfa.reference

import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode
import metal.immutable.HashMap

import scala.collection.mutable

class BuilderReferenceNode(val name: String,
                           val id: Int,
                           val layer: Int,
                           val fileOffset: Long,
                           val contentLength: Int,
                           val incomingEdges: mutable.Buffer[(Int, Long)],
                           val outgoingEdges: mutable.Buffer[(Int, Long)],
                           val coordinates: HashMap[Int, Long])
  extends ReferenceNode
    with GenomeCoordinate {

  def withIdAndLayer(_id: Int, _layer: Int): BuilderReferenceNode =
    new BuilderReferenceNode(name, _id, _layer, fileOffset, contentLength, incomingEdges, outgoingEdges, coordinates)
}

object BuilderReferenceNode {
  lazy val empty: BuilderReferenceNode = new BuilderReferenceNode("", -1, -1, -1, -1, mutable.Buffer.empty, mutable.Buffer.empty, HashMap())
}
