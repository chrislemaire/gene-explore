package com.clemaire.gexplore.util

import scala.collection.mutable

object Stopwatch {

  private val markers: mutable.Map[String, Long] =
    mutable.Map()

  private val relativeTimes: mutable.Map[String, Long] =
    mutable.Map()

  def initializeMarker(marker: String): Unit =
    markers.put(marker, 0L)

  def start(marker: String): Unit = {
    if (!markers.contains(marker)) {
      initializeMarker(marker)
    }
    relativeTimes.put(marker, System.currentTimeMillis())
  }

  def stop(marker: String): Unit = {
    markers(marker) += System.currentTimeMillis() - relativeTimes(marker)
  }

  def time(marker: String): Long =
    markers(marker)

}
