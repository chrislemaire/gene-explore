package com.clemaire.gexplore.core.gfa.coordinates.data

import com.clemaire.cache.definitions.Identifiable

trait Order
  extends Identifiable {

  def rank: Int = -1
}

trait MutableOrder
  extends Order {

  override var rank: Int
}

object Order {
  private class OrderImpl(val id: Int,
                          override val rank: Int)
    extends Order

  def apply(id: Int,
            rank: Int): Order =
    new OrderImpl(id, rank)
}
