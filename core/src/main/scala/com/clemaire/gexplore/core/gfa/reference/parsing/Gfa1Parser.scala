package com.clemaire.gexplore.core.gfa.reference.parsing

import java.util.InputMismatchException

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.parsing.Gfa1Parser._
import com.clemaire.gexplore.util.SimpleCheck.checkThatOrThrow

/**
  * Exception thrown when the length of some
  * line-split is not high enough.
  *
  * @param expected   The number of columns that
  *                   was expected.
  * @param forElement The element type that
  *                   is parsed for the line.
  * @param butWas     What the actual number
  *                   of split entries was.
  * @param forLine    For which line the exception
  *                   is raised.
  */
abstract class Gfa1ColumnLengthException(expected: Int,
                                         forElement: String,
                                         butWas: Int,
                                         forLine: String)
  extends RuntimeException(s"Expected at least $expected columns in $forElement,\n" +
    s"but there were only $butWas columns in line:\n$forLine")

/**
  * Exception thrown when the length of a
  * segment-line-split is not high enough.
  *
  * @param butWas  What the actual number
  *                of split entries was.
  * @param forLine For which line the exception
  *                is raised.
  */
case class Gfa1SegmentColumnLengthException(private val butWas: Int,
                                            private val forLine: String)
  extends Gfa1ColumnLengthException(SEG_MIN_LENGTH, "segment", butWas, forLine)

/**
  * Exception thrown when the length of a
  * link-line-split is not high enough.
  *
  * @param butWas  What the actual number
  *                of split entries was.
  * @param forLine For which line the exception
  *                is raised.
  */
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
  private[core] val LINK_OPTIONS_INDEX: Int = 6

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
                   from: Int): Traversable[(String, String)] =
    split.drop(from).map(parseOption)

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

}

/**
  * @param tags The set of tags that should be passed
  *             to registration for the options map.
  */
abstract class Gfa1Parser(val tags: Set[String]) {

  /**
    * The [[GraphBuilder]] to which parsed segments
    * and links will be parsed for registration.
    */
  private var cacheBuilder: GraphBuilder = _

  /**
    * Adjusts this [[Gfa1Parser]] to use the given
    * [[GraphBuilder]] and returns the adjusted [[Gfa1Parser]].
    *
    * @param builder The new builder to use for GFA element
    *                registration.
    * @return The [[Gfa1Parser]] using the given builder.
    */
  def withBuilder(builder: GraphBuilder): this.type = {
    cacheBuilder = builder
    this
  }

  /**
    * Parses the source GFA file given and arranges
    * for all segments and links in it to be registered
    * to the [[GraphBuilder]].
    *
    * @param paths The list of paths containing the
    *              source path.
    */
  def parse(paths: CachePathList): Unit

  /**
    * Parses the options provided from the end of a
    * line. Given is a line-split and the index from
    * which options can be read. Additionally, this
    * function filters the list of options in the given
    * set of tags early.
    *
    * @param split Line split from which options will
    *              have to be parsed.
    * @param from  Index from which on options should be
    *              parsed.
    * @return The map of options parsed.
    */
  def parseOptionsWithFilter(split: Array[String],
                             from: Int): Traversable[(String, String)] =
    split.drop(from)
      .filter(s => tags(s.substring(0, s.indexOf(':'))))
      .map(parseOption)

  /**
    * Parses a all lines of some GFA file and passes the
    * parsed element onto the given [[GraphBuilder]].
    *
    * @param input  The lines to parse GFA elements from.
    * @param offset The offset in the file at which this
    *               lines start.
    */
  protected def parseAllIn(input: String,
                           offset: Long): Unit =
    input.lines.foldLeft(offset)((offset, line) => {
      parseLine(line, offset)
      offset + line.length + 1
    })

  /**
    * Parses a single line of some GFA file and passes the
    * parsed element onto the given [[GraphBuilder]].
    *
    * @param line   The line to parse a GFA element from.
    * @param offset The offset in the file at which this
    *               line starts.
    */
  protected def parseLine(line: String,
                          offset: Long): Unit =
    line.trim.charAt(0) match {
      case 'L' => parseLink(line, offset)
      case 'S' => parseSegment(line, offset)
      case 'H' => parseHeader(line)
      case '#' =>
      case e => throw new InputMismatchException(s"Symbol '$e' not a valid start of a GFA line.")
    }

  /**
    * Parses a header string by splitting it and passing
    * the parsed options onto the given [[GraphBuilder]].
    *
    * @param headerString The String representing a header
    *                     that is to be parsed.
    */
  protected def parseHeader(headerString: String): Unit = {
    val split = headerString.split(DELIM_MAIN)
    cacheBuilder.registerHeader(parseOptions(split, 1))
  }

  /**
    * Parses a segment string by splitting it and passing
    * the relevant data onto the given [[GraphBuilder]].
    *
    * @param segString  The String representing a segment
    *                   that is to be parsed.
    * @param fileOffset The offset at which this line starts
    *                   in the parsed file.
    */
  protected def parseSegment(segString: String,
                             fileOffset: Long): Unit = {
    val split = segString.split(DELIM_MAIN)

    checkThatOrThrow(split.length >= SEG_MIN_LENGTH,
      Gfa1SegmentColumnLengthException(split.length, segString))

    cacheBuilder.registerSegment(fileOffset,
      split(SEG_NAME_INDEX),
      split(SEG_CONTENT_INDEX),
      parseOptionsWithFilter(split, SEG_OPTIONS_INDEX))
  }

  /**
    * Parses a link string by splitting it and passing
    * the relevant data onto the given [[GraphBuilder]].
    *
    * @param linkString The String representing a link
    *                   that is to be parsed.
    * @param fileOffset The offset at which this line starts
    *                   in the parsed file.
    */
  protected def parseLink(linkString: String,
                          fileOffset: Long): Unit = {
    val split = linkString.split(DELIM_MAIN)

    checkThatOrThrow(split.length >= LINK_MIN_LENGTH,
      Gfa1LinkColumnLengthException(split.length, linkString))

    cacheBuilder.registerLink(fileOffset,
      split(LINK_FROM_INDEX),
      split(LINK_TO_INDEX),
      parseOptionsWithFilter(split, LINK_OPTIONS_INDEX))
  }

}
