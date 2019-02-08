package com.clemaire.gexplore.core.gfa.reference.data

import com.clemaire.gexplore.core.gfa.data.Identifiable

case class GenomeCoordinate(id: Int,
                            coordinates: Map[Int, Long])
  extends Identifiable
