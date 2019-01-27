package com.clemaire.gexplore.core.gfa.reference.data

case class GenomeCoordinate(id: Int,
                            coordinates: Map[Int, Long])
  extends Indexed
