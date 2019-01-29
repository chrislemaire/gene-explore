package com.clemaire.gexplore.core.gfa.reference.reading

import com.clemaire.gexplore.core.gfa.reference.index.AbstractIndex

trait IndexReader[I <: AbstractIndex[_]] {

  def readIndex(): I

}
