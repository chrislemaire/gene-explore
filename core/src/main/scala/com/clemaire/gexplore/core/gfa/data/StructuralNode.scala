package com.clemaire.gexplore.core.gfa.data

import com.clemaire.cache.definitions.Identifiable

case class StructuralNode(id: Int,
                          layer: Int,
                          contentLength: Int,
                          incomingEdges: Set[Int],
                          outgoingEdges: Set[Int])
  extends Identifiable
