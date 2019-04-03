package com.clemaire.gexplore.core.gfa.coordinates.data

import com.clemaire.cache.definitions.Identifiable

trait Order
  extends Identifiable {

  def rank: Int = -1
}

trait MutableOrder
  extends Order {

  private[this] var _rank: Int = -1

  def rank_=(r: Int): Unit = _rank = r

  override def rank: Int = _rank
}

object Order {
  private class OrderImpl(val id: Int,
                          override val rank: Int)
    extends Order

  def apply(id: Int,
            rank: Int): Order =
    new OrderImpl(id, rank)
}
