package com.clemaire.gexplore.core.gfa.reference.coordinates

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.ReferenceNode
import com.clemaire.gexplore.core.gfa.reference.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.util.io.NioBufferedWriter

import scala.collection.mutable

class GenomeCoordinatesWriter(paths: CachePathList,
                              currentCoordinates: mutable.Map[Int, Long])
  extends AdditionalReferenceWriter
    with NioBufferedWriter
    with GenomeDataWriter {

  val lastIndexedCoordinates: mutable.Map[Int, Long] =
    new mutable.HashMap()

  val indexWriter: GenomeCoordinatesIndexWriter =
    new GenomeCoordinatesIndexWriter(paths, lastIndexedCoordinates, currentCoordinates)

  override def writeNode(node: ReferenceNode,
                         byteLength: Int): Unit = {
    indexWriter.writeNode(node, byteLength)

    val adjustedCoordinates = node.genomeCoordinates
      .map(kv => (kv._1, kv._2 - lastIndexedCoordinates(kv._1)))

    if (adjustedCoordinates.exists(_._2 > Int.MaxValue)) {
      adjustedCoordinates.foreach(kv => lastIndexedCoordinates(kv._1) = kv._2)

      checkForFlush(length(adjustedCoordinates))
      indexWriter.writeNode(node, byteLength)
    } else {
      write(node.id -> adjustedCoordinates, buffer)
    }
  }

}
