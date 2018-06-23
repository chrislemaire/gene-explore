package com.clemaire.gexplorer.core.gfa.reference.impl

import com.clemaire.gexplorer.core.gfa.data.{FullEdge, FullNode, Node}
import com.clemaire.gexplorer.core.gfa.reference.ReferenceCache

import scala.collection.mutable

class SimpleReferenceCache extends ReferenceCache {

  private[impl] val _genomeNames: mutable.Map[String, Int] =
    mutable.Map()

  private[impl] val _genomes: mutable.Map[Int, String] =
    mutable.Map()

  def genomeNames: Map[String, Int] = _genomeNames.toMap

  def genomes: Map[Int, String] = _genomes.toMap

  override def getFullNode(id: Int): FullNode = ???

  override def getFullNode(name: String): FullNode = ???

  override def getFullEdge(id: Int): FullEdge = ???

  override def getNode(id: Int): Node = ???

  override def getNode(name: String): Node = ???
}
