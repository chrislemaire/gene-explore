package com.clemaire.gexplore.core.gfa.reference.data

import scala.collection.mutable

case class ReferenceNode(id: Int,
                         layer: Int,
                         fileOffset: Long,
                         contentLength: Int,
                         incomingEdges: mutable.Buffer[(Int, Long)],
                         outgoingEdges: mutable.Buffer[(Int, Long)])
