package com.clemaire.genominator.core

import com.clemaire.genominator.core.Gfa1Splitter._
import com.clemaire.gexplore.util.SimpleCheck.checkThatOrThrow

object Gfa1Splitter {

  /**
    * Delimiter splitting a line of information.
    */
  private val DELIM_MAIN: String = "\t"

}

case class Gfa1ColumnLengthException(expected: Int,
                                     forElement: String,
                                     butWas: Int,
                                     forLine: String)
  extends RuntimeException(s"Expected $expected columns in $forElement,\n" +
    s"but there were $butWas columns in line:\n$forLine")

class Gfa1Splitter(val parser: Gfa1ElementParser) {

  private def splitSegment(segString: String): Unit = {
    val split = segString.split(DELIM_MAIN)

    checkThatOrThrow(split.length == 5,
      Gfa1ColumnLengthException(5, "segment", split.length, segString))

    parser.parseSegment(split(1), split(2), split(4))
  }

  private def splitLink(linkString: String): Unit = {
    val split = linkString.split(DELIM_MAIN)

    checkThatOrThrow(split.length == 6 || split.length == 7,
      Gfa1ColumnLengthException(7, "link", split.length, linkString))

    parser.parseLink(split(1), split(3))
  }

}
