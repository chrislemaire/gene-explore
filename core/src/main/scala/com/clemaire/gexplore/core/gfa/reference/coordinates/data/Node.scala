package com.clemaire.gexplore.core.gfa.reference.coordinates.data

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.AlternatingEntry
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.Container

trait Node
  extends AlternatingEntry[Container, Node]
    with Identifiable {

  var _measure: Double = -1.0

  override implicit def self2c(self: Node.this.type): Node = this

  def measure_=(m: Double): Unit = _measure = m
  override def measure: Double = _measure

  def remove(): Unit = {
    prev.get.next = next.flatMap(_.next)
    next.flatMap(_.next).foreach(_.prev = prev)

    if (next.isDefined)
      prev.get.join(next.get)
  }

  def replace(other: Node): Unit = {
    other.prev = prev
    other.next = next
  }

  val id: Int
  val layer: Int
  val incoming: Traversable[Node]
  val outgoing: Traversable[Node]

  def isPNode: Boolean = false
  def isQNode: Boolean = false
  def isRNode: Boolean = false
  def segment: Segment = null
}
