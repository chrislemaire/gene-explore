package com.clemaire.gexplore.core.gfa.reference.cache

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.writing.{ReferenceCacheBuilder, ReferenceNodeWriter}
import com.clemaire.gexplore.core.gfa.reference.writing.io.NioBufferedSRWriter

import scala.collection.mutable

class SRCacheBuilder(private val pathsIn: CachePathList)
  extends ReferenceCacheBuilder[SRCache](pathsIn) {

  /**
    * The cache being built by this [[SRCacheBuilder]].
    */
  private val cache: SRCache = new SRCache()

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
    new NioBufferedSRWriter(paths, this)

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
  private var currentNode: Option[ReferenceNode] = None

  override final def registerHeader(options: Traversable[(String, String)]): Unit =
    options.filter(_._1 == "ORI").foreach(_._2.split(";")
        .filterNot(gen => cache.genomeNames.contains(gen))
        .filterNot(_.isEmpty)
        .foreach(gen => {
          genomeIndex += 1
          cache._genomeNames += gen -> genomeIndex
          cache._genomes += genomeIndex -> gen
          genomeCoordinates(genomeIndex) = 0
        }))

  override final def registerLink(atOffset: Long,
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

    val node: ReferenceNode = currentNode.get
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

  override final def registerSegment(atOffset: Long,
                                     name: String,
                                     content: String,
                                     options: Traversable[(String, String)]): Unit = {
    writeCurrentNode()

    val nodeGenomes = getGenomes(options)

    val (id, layer) = lookupNode(name)
    currentNode = Some(ReferenceNode(name,
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
          cache._genomeNames(s)
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

  override final def finish(): SRCache = {
    try {
      writeCurrentNode()
      writer.flush()

      cache._index = writer.index
      cache._coordinatesIndex = writer.coordinatesIndex

      cache
    } finally {
      close()
    }
  }

  override def close(): Unit =
    writer.close()
}
