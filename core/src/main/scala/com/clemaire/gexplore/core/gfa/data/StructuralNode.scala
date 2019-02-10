package com.clemaire.gexplore.core.gfa.data

case class StructuralNode(id: Int,
                          layer: Int,
                          contentLength: Int,
                          incomingEdges: Set[Int],
                          outgoingEdges: Set[Int])
  extends Identifiable
    with Positional
