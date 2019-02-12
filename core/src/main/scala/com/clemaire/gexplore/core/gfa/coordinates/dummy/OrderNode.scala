package com.clemaire.gexplore.core.gfa.coordinates.dummy

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.gexplore.core.gfa.coordinates.data.MutableOrder
import com.clemaire.gexplore.core.gfa.Positional

trait OrderNode
  extends Identifiable
    with Positional
    with MutableOrder {

  /**
    * @return The list of outgoing node connections.
    *         Node connections are indicated by the connected
    *         node IDs.
    */
  def outgoing: Set[Int]

  /**
    * @return The list of incoming node connections.
    *         Node connections are indicated by the connected
    *         node IDs.
    */
  def incoming: Set[Int]

  /**
    * @return `true` when this [[OrderNode]] is a dummy-node,
    *         `false` otherwise.
    */
  def isDummy: Boolean = false
}

object OrderNode {
  private class OrderNodeImpl(val id: Int,
                              val layer: Int,
                              val incoming: Set[Int],
                              val outgoing: Set[Int])
    extends OrderNode

  def apply(id: Int,
            layer: Int,
            outgoing: Set[Int],
            incoming: Set[Int]): OrderNode =
    new OrderNodeImpl(id, layer, incoming, outgoing)
}
