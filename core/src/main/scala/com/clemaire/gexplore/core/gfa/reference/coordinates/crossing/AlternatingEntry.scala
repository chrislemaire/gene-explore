package com.clemaire.gexplore.core.gfa.reference.coordinates.crossing

trait AlternatingEntry[N <: AlternatingEntry[C, N], C <: AlternatingEntry[N, C]] {
  var prev: Option[N] = None
  var next: Option[N] = None

  var position: Int = -1
  def measure: Double = position

  implicit def self2c(self: this.type): C = self.asInstanceOf[C]

  def append(entry: N): N = append(Option(entry)).orNull(null[N])
  def append(entry: Option[N]): Option[N] = {
    entry.foreach(_.prev = Some(this))
    next = entry

    entry
  }

  def foreach(fc: C => Any)(fn: N => Any): Unit = {
    fc(this)
    next.foreach(_.foreach(fn)(fc))
  }

  def map[T](fc: C => T)(fn: N => T): List[T] = {
    fc(this) :: next.fold[List[T]](Nil)(_.map(fn)(fc))
  }

}