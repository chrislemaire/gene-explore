package com.clemaire.gexplore.core.gfa.data

case class Node(id: Int,
                name: String,
                content: String,
                genomeCoordinates: Map[Int, Long])
