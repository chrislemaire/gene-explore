package com.clemaire.gexplore.core.gfa.reference.node

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.gexplore.core.gfa.Positional

trait ReferenceNode
  extends Identifiable
    with Positional {

  def fileOffset: Long
  def contentLength: Int

  val incomingEdges: Traversable[(Int, Long)]
  val outgoingEdges: Traversable[(Int, Long)]
}

object ReferenceNode {
  private case class ReferenceNodeImpl
  (id: Int,
   layer: Int,
   fileOffset: Long,
   contentLength: Int,
   incomingEdges: Traversable[(Int, Long)],
   outgoingEdges: Traversable[(Int, Long)]) extends ReferenceNode

  def apply(id: Int,
            layer: Int,
            fileOffset: Long,
            contentLength: Int,
            incomingEdges: Traversable[(Int, Long)],
            outgoingEdges: Traversable[(Int, Long)]): ReferenceNode =
    ReferenceNodeImpl(id, layer, fileOffset, contentLength, incomingEdges, outgoingEdges)

}
