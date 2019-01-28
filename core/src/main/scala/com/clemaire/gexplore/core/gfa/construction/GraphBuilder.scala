package com.clemaire.gexplore.core.gfa.construction

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.cache.Cache
import com.clemaire.gexplore.core.gfa.data.GraphData
import com.clemaire.gexplore.core.gfa.parsing.Gfa1Parser
import com.clemaire.gexplore.core.gfa.reference.data.BuilderReferenceNode
import com.clemaire.gexplore.core.gfa.reference.writing.ReferenceNodeWriter
import com.clemaire.gexplore.core.gfa.reference.writing.io.NioBufferedSRWriter

import scala.collection.mutable

class GraphBuilder(val paths: CachePathList) {

  /**
    * The mapping of genomes to their indices, or
    * identifiers.
    */
  private val genomeNames: mutable.Map[String, Int] =
    mutable.HashMap()

  /**
    * The mapping of genomes to their indices, or
    * identifiers.
    */
  private val genomes: mutable.Map[Int, String] =
    mutable.HashMap()

  /**
    * The segments that are to come paired with the
    * layers they have been assigned to.
    */
  private val lookAheadSegments: mutable.Map[String, (Int, Int)] = mutable.Map()

  /**
    * Map keeping track of incoming edges (value) for a
    * every upcoming node (key).
    */
  private val incomingEdges: mutable.Map[Int, mutable.Buffer[(Int, Long)]] =
    mutable.Map()

  /**
    * The mapping of genome IDs to their respective
    * current coordinate.
    */
  val genomeCoordinates: mutable.Map[Int, Long] =
    mutable.HashMap()

  /**
    * Writer to write the node-reference data to file.
    */
  private val writer: ReferenceNodeWriter =
    new NioBufferedSRWriter(this)

  /**
    * The number of genomes currently counted.
    */
  private var genomeIndex = -1

  /**
    * The number of segments currently counted.
    */
  private var segmentIndex = -1

  /**
    * The current node being built by the reference
    * builder wrapped in an option.
    */
  private var currentNode: Option[BuilderReferenceNode] = None

  /**
    * Registers a header that is identified
    * by the list of options it defines. The
    * built [[GraphData]] should hereafter
    * recognize the options provided as header
    * options.
    *
    * @param options The options defining
    *                the header.
    */
  final def registerHeader(options: Traversable[(String, String)]): Unit =
    options.filter(_._1 == "ORI").foreach(_._2.split(";")
      .filterNot(gen => genomeNames.contains(gen))
      .filterNot(_.isEmpty)
      .foreach(gen => {
        genomeIndex += 1
        genomeNames += gen -> genomeIndex
        genomes += genomeIndex -> gen
        genomeCoordinates(genomeIndex) = 0
      }))

  /**
    * Registers a link that is identified by its position
    * in the source file, the node it starts in and the node
    * it goes to. The built [[GraphData]] should hereafter
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
  final def registerLink(atOffset: Long,
                         from: String,
                         to: String,
                         options: Traversable[(String, String)]): Unit = {
    val (fromId, fromLayer) = lookupNode(from)
    val (toId, toLayer) = if (lookAheadSegments.contains(to)) {
      val (toId, toLayer) = lookupNode(to)
      (toId, Math.max(toLayer, fromLayer + 1))
    } else {
      segmentIndex += 1
      (segmentIndex, fromLayer + 1)
    }

    lookAheadSegments.put(to, (toId, toLayer))

    val node: BuilderReferenceNode = currentNode.get
    node.outgoingEdges += toId -> atOffset

    if (incomingEdges.contains(toId)) {
      incomingEdges(toId) += fromId -> atOffset
    } else {
      incomingEdges.put(toId, mutable.Buffer((fromId, node.fileOffset)))
    }
  }

  /**
    * Looks up a node with the given name and finds
    * the associated node-ID and layer.
    *
    * @param name The name of the node to find.
    * @return A tuple containing the node-ID and layer
    *         of the node.
    */
  private def lookupNode(name: String): (Int, Int) =
    if (lookAheadSegments.contains(name)) {
      lookAheadSegments(name)
    } else {
      lookAheadSegments.put(name, (segmentIndex, 0))
      segmentIndex += 1
      (segmentIndex, 0)
    }

  /**
    * Registers a segment that is identified by its position
    * in the source file, its name, its content, and the options
    * provided to it. The build [[Cache]] should hereafter
    * recognize the segment provided as such.
    *
    * @param atOffset The position in the file at which the
    *                 node's description starts.
    * @param name     The name of the node.
    * @param content  The content that the node describes.
    * @param options  The options provided to the node.
    */
  final def registerSegment(atOffset: Long,
                            name: String,
                            content: String,
                            options: Traversable[(String, String)]): Unit = {
    writeCurrentNode()

    val nodeGenomes = getGenomes(options)

    val (id, layer) = lookupNode(name)
    currentNode = Some(BuilderReferenceNode(name,
      id, layer, atOffset, content.length,
      incomingEdges.getOrElse(id, mutable.Buffer()),
      mutable.Buffer(),
      nodeGenomes.map(gen => gen -> genomeCoordinates(gen))))

    nodeGenomes.foreach(gen => genomeCoordinates(gen) += content.length)
  }

  /**
    * Searches the given mapping of options for an
    * ORI tag and maps its values to an array of genome
    * indices.
    *
    * @param options The options to extract a list of
    *                genomes from.
    * @return The array of genomes that is extracted from
    *         the given options.
    */
  private def getGenomes(options: Traversable[(String, String)]): Array[Int] =
    options.headOption.map(_._2.split(';')
      .filterNot(_.isEmpty)
      .map(s => {
        if (s forall Character.isDigit) {
          s.toInt
        } else {
          genomeNames(s)
        }
      })).getOrElse(Array.empty)

  /**
    * Writes node reference data to file through the
    * accompanying [[writer]] and clears any auxiliary
    * data kept on the current node.
    */
  protected def writeCurrentNode(): Unit =
    currentNode.foreach(node => {
      writer.write(node)

      incomingEdges.remove(node.id)
      lookAheadSegments.remove(node.name)

      currentNode = None
    })

  /**
    * Finishes building the graph and closes this
    * [[GraphBuilder]].
    */
  final def finish(): Unit = {
    try {
      writeCurrentNode()
      writer.flush()

      GraphData(paths,
        writer.index,
        writer.coordinatesIndex,
        genomes.toMap)
    } finally {
      close()
    }
  }

  /**
    * Builds the graph with the given [[Gfa1Parser]]
    * as its parser and the given [[CachePathList]] to find the
    * source and output files.
    *
    * @param parser The parser to use for parsing the GFA file.
    * @return The finished cache.
    */
  def buildWith(parser: Gfa1Parser): Unit = {
    parser.withBuilder(this).parse(paths)
    finish()
  }

  def close(): Unit =
    writer.close()

}
