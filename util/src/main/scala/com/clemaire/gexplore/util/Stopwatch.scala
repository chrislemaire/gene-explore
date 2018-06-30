package com.clemaire.gexplore.util

import scala.collection.mutable

object Stopwatch {

  /**
    * A mapping of marker names to their current total times.
    */
  private val totalTimes: mutable.Map[String, Long] =
    mutable.Map()

  /**
    * A mapping of marker names to the times at which they
    * were last started.
    */
  private val startTimes: mutable.Map[String, Long] =
    mutable.Map()

  /**
    * Resets the time for a marker with the given name.
    * After a reset of marker {{{m}}} {{{time(m)}}} should
    * give back {{{0}}} and {{{stop(m)}}} should throw an
    * exception.
    * @param marker The name of the marker to reset.
    */
  def reset(marker: String): Unit = {
    totalTimes.put(marker, 0L)
    startTimes.remove(marker)
  }

  /**
    * Starts the time for the given marker by storing
    * the current time for it.
    * @param marker The name of the time marker to
    *               start.
    */
  def start(marker: String): Unit = {
    if (!totalTimes.contains(marker)) {
      reset(marker)
    }
    startTimes.put(marker, System.currentTimeMillis())
  }

  /**
    * Stops the time for the given marker by taking
    * the last start time and adding the difference
    * between it and the current time to the total
    * time of the given marker.
    * @param marker The name of the time marker to
    *               stop.
    */
  def stop(marker: String): Unit =
    totalTimes(marker) += System.currentTimeMillis() - startTimes(marker)

  /**
    * @param marker The marker to get the time of.
    * @return The current time for the given marker.
    */
  def timeFor(marker: String): Long =
    totalTimes(marker)

  override def toString: String =
    totalTimes.map(kv => kv._1 + "\t= " + kv._2).mkString("\n")

}
