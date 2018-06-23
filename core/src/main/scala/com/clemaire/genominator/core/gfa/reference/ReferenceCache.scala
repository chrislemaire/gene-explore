package com.clemaire.genominator.core.gfa.reference

import com.clemaire.genominator.core.gfa.data.{FullEdge, FullNode, Node}

import scala.collection.mutable

trait ReferenceCache {

  private[ReferenceBuilder] val _genomeNames: mutable.Map[String, Int] =
    mutable.Map()

  private[ReferenceBuilder] val _genomes: mutable.Map[Int, String] =
    mutable.Map()

  def genomeNames: Map[String, Int] = _genomeNames.toMap

  def genomes: Map[Int, String] = _genomes.toMap

  def getFullNode(id: Int): FullNode

  def getFullNode(name: String): FullNode

  def getFullEdge(id: Int): FullEdge

  def getNode(id: Int): Node

  def getNode(name: String): Node

}
