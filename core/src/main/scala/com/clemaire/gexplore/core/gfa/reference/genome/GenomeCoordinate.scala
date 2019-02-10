package com.clemaire.gexplore.core.gfa.reference.genome

import com.clemaire.cache.definitions.Identifiable

trait GenomeCoordinate
  extends Identifiable {

  val id: Int
  val coordinates: Map[Int, Long]
}

object GenomeCoordinate {
  private case class GenomeCoordinateImpl
  (id: Int,
   coordinates: Map[Int, Long]) extends GenomeCoordinate

  def apply(id: Int, coordinates: Map[Int, Long]): GenomeCoordinate =
    GenomeCoordinateImpl(id, coordinates)
}
