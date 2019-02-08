package com.clemaire.gexplore.core.gfa.reference.coordinates.io.cache

import com.clemaire.gexplore.core.gfa.data.Identifiable

case class IndexedData[D](id: Int,
                          data: D)
  extends Identifiable
