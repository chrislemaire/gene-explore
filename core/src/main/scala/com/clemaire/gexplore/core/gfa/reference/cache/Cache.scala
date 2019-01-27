package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.data.Node

trait Cache {

  def nodesBetweenLayers(left: Int,
                         right: Int): Traversable[Node]

}
