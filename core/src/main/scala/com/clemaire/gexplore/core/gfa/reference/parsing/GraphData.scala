package com.clemaire.gexplore.core.gfa.reference.parsing

import com.clemaire.gexplore.core.gfa.reference.index.{GCIndex, SRIndex}

import scala.collection.mutable

class GraphData {

  /**
    * The index of reference nodes used to retrieve
    * chunks of nodes by their layer or segment overlap.
    */
  private[parsing] var _index: SRIndex = _

  /**
    * The index of genome coordinates used to retrieve
    * chunks of genome coordinates by the layer or
    * segment ID the corresponding nodes have.
    */
  private[parsing] var _coordinatesIndex: GCIndex = _

  /**
    * The mapping of genomes to their indices, or
    * identifiers.
    */
  private[parsing] val _genomeNames: mutable.Map[String, Int] =
    mutable.HashMap()

  /**
    * The mapping of genomes to their indices, or
    * identifiers.
    */
  private[parsing] val _genomes: mutable.Map[Int, String] =
    mutable.HashMap()

  def genomeNames: Map[String, Int] = _genomeNames.toMap

  def genomes: Map[Int, String] = _genomes.toMap

}