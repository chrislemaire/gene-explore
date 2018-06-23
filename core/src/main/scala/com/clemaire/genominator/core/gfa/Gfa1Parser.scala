package com.clemaire.genominator.core.gfa

import com.clemaire.genominator.core.gfa.Gfa1Parser._
import com.clemaire.genominator.core.gfa.reference.ReferenceBuilder
import com.clemaire.gexplore.util.SimpleCheck.checkThatOrThrow

case class Gfa1ColumnLengthException(expected: Int,
                                     forElement: String,
                                     butWas: Int,
                                     forLine: String)
  extends RuntimeException(s"Expected $expected columns in $forElement,\n" +
    s"but there were $butWas columns in line:\n$forLine")


case class Gfa1SegmentColumnLengthException(private val butWas: Int,
                                            private val forLine: String)
  extends Gfa1ColumnLengthException(SEG_MIN_LENGTH, "segment", butWas, forLine)


case class Gfa1LinkColumnLengthException(private val butWas: Int,
                                         private val forLine: String)
  extends Gfa1ColumnLengthException(LINK_MIN_LENGTH, "link", butWas, forLine)


object Gfa1Parser {

  /**
    * Delimiter splitting a line of information.
    */
  private[core] val DELIM_MAIN: String = "\t"

  // Segment data column indices.
  private[core] val SEG_MIN_LENGTH: Int = 4
  private[core] val SEG_NAME_INDEX: Int = 1
  private[core] val SEG_CONTENT_INDEX: Int = 2
  private[core] val SEG_OPTIONS_INDEX: Int = 4

  // Link data column indices.
  private[core] val LINK_MIN_LENGTH: Int = 5
  private[core] val LINK_FROM_INDEX: Int = 1
  private[core] val LINK_TO_INDEX: Int = 3
  private[core] val LINK_OPTIONS_INDEX: Int = 5

  /**
    * Parses a single option and returns a tuple
    * representing the tag, type and value of that
    * option.
    *
    * @param option String representing a single option.
    * @return Tuple representing an option object.
    */
  private def parseOption(option: String): (String, String) = {
    val optionSplit = option.split(":")
    (optionSplit(0), optionSplit(2))
  }

  /**
    * Parses the options provided from the end of a
    * line. Given is a line-split and the index from
    * which options can be read.
    *
    * @param split Line split from which options will
    *              have to be parsed.
    * @param from  Index from which on options should be
    *              parsed.
    * @return The map of options parsed.
    */
  def parseOptions(split: Array[String],
                   from: Int): Map[String, String] =
    split.drop(from).map(parseOption).toMap

}

class Gfa1Parser(val cacheBuilder: ReferenceBuilder[_]) {

  protected def parseHeader(headerString: String): Unit = {
    val split = headerString.split(DELIM_MAIN)
    cacheBuilder.registerHeader(parseOptions(split, 1))
  }

  protected def parseSegment(segString: String,
                             fileOffset: Long): Unit = {
    val split = segString.split(DELIM_MAIN)

    checkThatOrThrow(split.length >= SEG_MIN_LENGTH,
      Gfa1SegmentColumnLengthException(split.length, segString))

    if (split.length > SEG_MIN_LENGTH) {
      cacheBuilder.registerSegment(fileOffset,
        split(SEG_NAME_INDEX),
        split(SEG_CONTENT_INDEX),
        parseOptions(split, SEG_OPTIONS_INDEX))
    } else {
      cacheBuilder.registerSegment(fileOffset,
        split(SEG_NAME_INDEX),
        split(SEG_CONTENT_INDEX))
    }
  }

  protected def parseLink(linkString: String,
                          fileOffset: Long): Unit = {
    val split = linkString.split(DELIM_MAIN)

    checkThatOrThrow(split.length >= LINK_MIN_LENGTH,
      Gfa1LinkColumnLengthException(split.length, linkString))

    if (split.length > LINK_MIN_LENGTH) {
      cacheBuilder.registerLink(fileOffset,
        split(LINK_FROM_INDEX),
        split(LINK_TO_INDEX),
        parseOptions(split, LINK_OPTIONS_INDEX))
    } else {
      cacheBuilder.registerLink(fileOffset,
        split(LINK_FROM_INDEX),
        split(LINK_TO_INDEX))
    }
  }

  protected def parseLine(line: String,
                          offset: Long): Unit = {
    line.trim.charAt(0) match {
      case 'H' => parseHeader(line)
      case 'S' => parseSegment(line, offset)
      case 'L' => parseLink(line, offset)
    }
  }

  protected def parseAllIn(input: String,
                           fileOffset: Long): Unit = {
    input.lines.foldLeft(fileOffset)((offset, line) => {
      parseLine(line, offset)
      offset + line.length + 1
    })
  }

}
