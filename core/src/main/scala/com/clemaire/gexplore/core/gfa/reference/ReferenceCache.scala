package com.clemaire.gexplore.core.gfa.reference

import com.clemaire.gexplore.core.gfa.data.{FullEdge, FullNode, Node}

trait ReferenceCache {

  def getFullNode(id: Int): FullNode

  def getFullNode(name: String): FullNode

  def getFullEdge(id: Int): FullEdge

  def getNode(id: Int): Node

  def getNode(name: String): Node

}