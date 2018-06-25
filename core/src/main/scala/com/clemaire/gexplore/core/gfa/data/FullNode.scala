package com.clemaire.gexplore.core.gfa.data

case class FullNode(id: Int,
                    name: String,
                    content: String,
                    incomingEdges: List[Int],
                    outgoingEdges: List[Int],
                    options: Map[String, String],
                    genomeCoordinates: Map[Int, Long])
