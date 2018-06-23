package com.clemaire.genominator.core

trait Gfa1ElementParser {

  def parseSegment(name: String,
                   content: String,
                   options: String): Unit

  def parseLink(from: String,
                to: String): Unit

}
