package com.clemaire.gexplore.core.gfa.reference.data

import com.clemaire.gexplore.core.gfa.data.{Identifiable, Positional}

case class ReferenceNode(id: Int,
                         layer: Int,
                         fileOffset: Long,
                         contentLength: Int,
                         incomingEdges: Traversable[(Int, Long)],
                         outgoingEdges: Traversable[(Int, Long)])
  extends Identifiable
    with Positional
