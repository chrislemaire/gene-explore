package com.clemaire.gexplore.core.gfa.reference.data

import com.clemaire.gexplore.core.gfa.data.Indexed

case class GenomeCoordinate(id: Int,
                            coordinates: Map[Int, Long])
  extends Indexed
