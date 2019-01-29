package com.clemaire.gexplore.core.gfa.data

case class GraphHeader(options: Map[String, String],
                       genomes: Map[Int, String],
                       coordinateMax: Map[Int, Long])
