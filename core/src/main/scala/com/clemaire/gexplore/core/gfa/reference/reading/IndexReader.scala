package com.clemaire.gexplore.core.gfa.reference.reading

import com.clemaire.gexplore.core.gfa.reference.index.Index

trait IndexReader[I <: Index[_]] {

  def readIndex(): I

}
