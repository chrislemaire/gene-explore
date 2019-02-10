package com.clemaire.gexplore.core.gfa.reference

import com.clemaire.gexplore.core.gfa.reference.genome.GenomeCoordinate
import com.clemaire.gexplore.core.gfa.reference.node.ReferenceNode

import scala.collection.mutable

case class BuilderReferenceNode(name: String,
                                id: Int,
                                layer: Int,
                                fileOffset: Long,
                                contentLength: Int,
                                incomingEdges: mutable.Buffer[(Int, Long)],
                                outgoingEdges: mutable.Buffer[(Int, Long)],
                                coordinates: Map[Int, Long])
  extends ReferenceNode
    with GenomeCoordinate

object BuilderReferenceNode {
  lazy val empty: BuilderReferenceNode = BuilderReferenceNode("", -1, -1, -1, -1, mutable.Buffer.empty, mutable.Buffer.empty, Map.empty)
}
