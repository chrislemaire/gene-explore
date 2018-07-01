package com.clemaire.gexplore.core.gfa.reference.writing

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceCache
import com.clemaire.gexplore.core.gfa.reference.parsing.Gfa1Parser

abstract class ReferenceCacheBuilder[+T <: ReferenceCache](val paths: CachePathList)
  extends AutoCloseable {

  /**
    * Registers a header that is identified
    * by the list of options it defines. The
    * built [[ReferenceCache]] should hereafter
    * recognize the options provided as header
    * options.
    *
    * @param options The options defining
    *                the header.
    */
  def registerHeader(options: Traversable[(String, String)]): Unit

  /**
    * Registers a link that is identified by its position
    * in the source file, the node it starts in and the node
    * it goes to. The built [[ReferenceCache]] should hereafter
    * recognize the link provided as such.
    *
    * @param atOffset The position in the file at which the
    *                 edge's description starts.
    * @param from     The name of the node at which this edge
    *                 starts.
    * @param to       The name of the node at which this edge
    *                 ends.
    * @param options  The options provided to the edge.
    */
  def registerLink(atOffset: Long,
                   from: String,
                   to: String,
                   options: Traversable[(String, String)] = Map()): Unit

  /**
    * Registers a segment that is identified by its position
    * in the source file, its name, its content, and the options
    * provided to it. The build [[ReferenceCache]] should hereafter
    * recognize the segment provided as such.
    *
    * @param atOffset The position in the file at which the
    *                 node's description starts.
    * @param name     The name of the node.
    * @param content  The content that the node describes.
    * @param options  The options provided to the node.
    */
  def registerSegment(atOffset: Long,
                      name: String,
                      content: String,
                      options: Traversable[(String, String)] = Map()): Unit

  /**
    * Finishes building the [[ReferenceCache]] and closes the
    * [[ReferenceCacheBuilder]].
    *
    * @return The built [[ReferenceCache]].
    */
  def finish(): T

  /**
    * Builds the [[ReferenceCache]] with the given [[Gfa1Parser]]
    * as its parser and the given [[CachePathList]] to find the
    * source and output files.
    *
    * @param parser The parser to use for parsing the GFA file.
    * @return The finished cache.
    */
  def buildWith(parser: Gfa1Parser): T = {
    parser.withBuilder(this).parse(paths)
    finish()
  }

}
