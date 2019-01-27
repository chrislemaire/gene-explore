package com.clemaire.gexplore.core.gfa.reference.data

case class ReferenceNode(id: Int,
                         layer: Int,
                         fileOffset: Long,
                         contentLength: Int,
                         incomingEdges: Traversable[(Int, Long)],
                         outgoingEdges: Traversable[(Int, Long)])
