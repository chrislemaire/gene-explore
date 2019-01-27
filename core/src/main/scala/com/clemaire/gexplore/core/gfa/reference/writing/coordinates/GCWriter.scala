package com.clemaire.gexplore.core.gfa.reference.writing.coordinates

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.data.BuilderReferenceNode
import com.clemaire.gexplore.core.gfa.reference.writing.additional.AdditionalReferenceWriter
import com.clemaire.gexplore.core.gfa.reference.writing.coordinates.data.GCDataWriter
import com.clemaire.gexplore.util.io.NioBufferedWriter

import scala.collection.mutable

class GCWriter(paths: CachePathList,
               currentCoordinates: mutable.Map[Int, Long])
  extends AdditionalReferenceWriter
    with NioBufferedWriter
    with GCDataWriter {

  private val _: Unit = {
    withPath(paths.coordinatesPath)
  }

  /**
    * Mapping of genome indices to the coordinates that
    * were last indexed for them.
    */
  val lastIndexedCoordinates: mutable.Map[Int, Long] =
    new mutable.HashMap()

  /**
    * Writer to write the coordinates index.
    */
  val indexWriter: GCIndexWriter =
    new GCIndexWriter(paths, lastIndexedCoordinates, currentCoordinates)

  override def writeNode(node: BuilderReferenceNode,
                         byteLength: Int): Unit = {
    indexWriter.writeNode(node, byteLength)

    val adjustedCoordinates = node.genomeCoordinates
      .map(kv => (kv._1, kv._2 - lastIndexedCoordinates.getOrElseUpdate(kv._1, kv._2)))

    if (adjustedCoordinates.exists(_._2 > Int.MaxValue)) {
      adjustedCoordinates.foreach(kv => lastIndexedCoordinates(kv._1) = kv._2)

      indexWriter.writeNode(node, byteLength)
    } else {
      checkForFlush(length(adjustedCoordinates))
      write(node.id -> adjustedCoordinates, buffer)
    }
  }

  override def flush(): Unit = {
    super.flush()
    indexWriter.flush()
  }

  override def close(): Unit = {
    super.close()
    indexWriter.close()
  }
}
