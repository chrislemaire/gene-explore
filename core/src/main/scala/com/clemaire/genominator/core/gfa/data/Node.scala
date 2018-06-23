package com.clemaire.genominator.core.gfa.data

case class Node(id: Int,
                name: String,
                content: String,
                genomeCoordinates: Map[Int, Long])
