package com.clemaire.gexplore.core.gfa.reference.data

import com.clemaire.gexplore.core.gfa.data.Positional

import scala.collection.mutable

case class BuilderReferenceNode(name: String,
                                id: Int,
                                layer: Int,
                                fileOffset: Long,
                                contentLength: Int,
                                incomingEdges: mutable.Buffer[(Int, Long)],
                                outgoingEdges: mutable.Buffer[(Int, Long)],
                                genomeCoordinates: Traversable[(Int, Long)])
  extends Positional

object BuilderReferenceNode {
  lazy val empty: BuilderReferenceNode = BuilderReferenceNode("", -1, -1, -1, -1, mutable.Buffer.empty, mutable.Buffer.empty, Map.empty)
}
