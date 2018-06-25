package com.clemaire.gexplore.core.gfa.reference

import scala.collection.mutable

case class ReferenceNode(name: String,
                         id: Int,
                         layer: Int,
                         fileOffset: Long,
                         contentLength: Int,
                         incomingEdges: mutable.Buffer[(Int, Long)],
                         outgoingEdges: mutable.Buffer[(Int, Long)],
                         genomeCoordinates: Array[(Int, Long)])

object ReferenceNode {
  val empty: ReferenceNode = ReferenceNode(
    "",
    -1,
    -1,
    -1,
    -1,
    mutable.Buffer.empty,
    mutable.Buffer.empty,
    Array.empty)
}
