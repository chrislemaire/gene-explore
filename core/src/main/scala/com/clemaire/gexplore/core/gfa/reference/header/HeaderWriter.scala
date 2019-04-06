package com.clemaire.gexplore.core.gfa.reference.header

import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.GraphHeader

import scala.collection.mutable

class HeaderWriter(val paths: CachePathList)
  extends AutoCloseable {

  private val _fc: FileChannel = FileChannel.open(paths.headerPath,
    StandardOpenOption.CREATE, StandardOpenOption.WRITE)

  private[this] val _options: mutable.Map[String, String] =
    mutable.HashMap()
  private[this] val _genomes: mutable.Map[Int, String] =
    mutable.HashMap()
  private[this] val _genomeMaxes: mutable.Map[Int, Long] =
    mutable.HashMap()


  def data = GraphHeader(_options.toMap, _genomes.toMap, _genomeMaxes.toMap)


  private[this] def stringifyOptions(): String =
    _options.map(kv => s"${kv._1}\t${kv._2}")
      .mkString(start = s"${_options.size}\n", sep = "\n", end = "")

  private[this] def stringifyGenomes(): String =
    _genomes.map(kv => s"${kv._1}\t${kv._2}\t${_genomeMaxes.getOrElse(kv._1, 0)}")
      .mkString(start = s"${_genomes.size}\n", sep = "\n", end = "")

  private[this] def stringifyAll(): String =
    s"${stringifyOptions()}\n${stringifyGenomes()}"

  def write(option: (String, String)): Unit =
    _options += option

  def write(options: Traversable[(String, String)]): Unit =
    _options ++= options

  def write(genomes: Map[Int, String]): Unit =
    _genomes ++= genomes

  def writeMaxCoords(genomeMaxCoords: Map[Int, Long]): Unit =
    _genomeMaxes ++= genomeMaxCoords

  def flush(): Unit = {
    val output: String = stringifyAll()
    _fc.write(ByteBuffer.wrap(output.getBytes()))
  }

  override def close(): Unit =
    _fc.close()

}
