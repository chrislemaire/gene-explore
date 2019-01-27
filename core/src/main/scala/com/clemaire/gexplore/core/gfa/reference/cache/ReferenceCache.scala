package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.data.Node

trait ReferenceCache {

  def nodesBetweenLayers(left: Int,
                         right: Int): Traversable[Node]

}
