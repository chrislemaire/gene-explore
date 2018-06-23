package com.clemaire.gexplorer.core.gfa.reference.cache

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.{ReferenceBuilder, ReferenceNode}
import com.clemaire.gexplorer.core.gfa.reference.io.SimpleBufferedReferenceWriter

import scala.collection.mutable

class SimpleReferenceBuilder(val paths: CachePathList)
  extends ReferenceBuilder[SimpleReferenceCache]
    with AutoCloseable {

  /**
    * The cache being built by this [[SimpleReferenceBuilder]].
    */
  private val cache: SimpleReferenceCache = new SimpleReferenceCache()

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

  /**
    * The mapping of genomes to their indices, or
    * identifiers.
    */
  private val genomes: mutable.Map[String, Int] =
    mutable.HashMap[String, Int]()

  /**
    * The mapping of genome IDs to their respective
    * current coordinate.
    */
  private val genomeCoordinates: mutable.Map[Int, Long] =
    mutable.HashMap[Int, Long]()

  /**
    * Writer to write the node-reference data to file.
    */
  private val writer: SimpleBufferedReferenceWriter =
    new SimpleBufferedReferenceWriter(paths)

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
  private def getGenomes(options: Map[String, String]): Array[Int] =
    options.getOrElse("ORI", "").split(';')
      .filterNot(_.isEmpty)
      .map(s => {
        if (s forall Character.isDigit) {
          s.toInt
        } else {
          genomes(s)
        }
      })

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

  override final def registerHeader(options: Map[String, String]): Unit =
    options.filter(_._1 == "ORI").values
      .foreach(value => value.split(";")
        .filterNot(gen => cache.genomeNames.contains(gen))
        .foreach(gen => {
          genomeIndex += 1
          cache._genomeNames += gen -> genomeIndex
          cache._genomes += genomeIndex -> gen
        }))

  override final def registerLink(atOffset: Long,
                                  from: String,
                                  to: String,
                                  options: Map[String, String]): Unit = {
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

  override final def registerSegment(atOffset: Long,
                                     name: String,
                                     content: String,
                                     options: Map[String, String]): Unit = {
    writeCurrentNode()

    val nodeGenomes = getGenomes(options)

    val (id, layer) = lookupNode(name)
    currentNode = Some(ReferenceNode(name,
      id,
      layer,
      atOffset,
      content.length,
      incomingEdges.getOrElse(id, mutable.Buffer()),
      mutable.Buffer(),
      nodeGenomes.map(gen => (gen, genomeCoordinates(gen)))))

    nodeGenomes.foreach(gen => genomeCoordinates(gen) += content.length)
  }

  override final def finish(): SimpleReferenceCache = {
    writeCurrentNode()
    writer.flush()

    cache
  }

  override def close(): Unit =
    writer.close()
}
