package com.clemaire.genominator.core.gfa.reference

import com.clemaire.genominator.core.gfa.data.{FullEdge, FullNode, Node}

class SimpleReferenceCache extends ReferenceCache {
  override def getFullNode(id: Int): FullNode = ???

  override def getFullNode(name: String): FullNode = ???

  override def getFullEdge(id: Int): FullEdge = ???

  override def getNode(id: Int): Node = ???

  override def getNode(name: String): Node = ???
}
