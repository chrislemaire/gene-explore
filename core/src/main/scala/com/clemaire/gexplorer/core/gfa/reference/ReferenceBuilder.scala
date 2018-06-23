package com.clemaire.gexplorer.core.gfa.reference

trait ReferenceBuilder[+T <: ReferenceCache] {

  def registerHeader(options: Map[String, String]): Unit

  def registerLink(atOffset: Long,
                   from: String,
                   to: String,
                   options: Map[String, String] = Map()): Unit

  def registerSegment(atOffset: Long,
                      name: String,
                      content: String,
                      options: Map[String, String] = Map()): Unit

  def finish(): T

}
