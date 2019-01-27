package com.clemaire.gexplore.core.gfa.reference.data

import com.clemaire.gexplore.core.gfa.data.Positional

case class StructuralNode(id: Int,
                          layer: Int,
                          contentLength: Int,
                          incomingEdges: Set[Int],
                          outgoingEdges: Set[Int])
  extends Positional
