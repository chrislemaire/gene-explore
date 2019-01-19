package com.clemaire.gexplore.core.gfa.data

case class StructuralNode(id: Int,
                          layer: Int,
                          fileOffset: Long,
                          contentLength: Int,
                          incomingEdges: Traversable[(Int, Long)],
                          outgoingEdges: Traversable[(Int, Long)])
