package com.clemaire.gexplore.core.gfa.reference.header

import java.nio.file.Files

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.data.GraphHeader

class HeaderReader(paths: CachePathList) {

  def read(): GraphHeader = {
    val lines = Files.readAllLines(paths.headerPath)

    var index = 0
    val options = (1 to lines.get(0).toInt)
      .map(i => lines.get(i).split("\t"))
      .map(split => split(0) -> split(1))
      .toMap

    index += 1 + options.size
    val genomeData = (1 to lines.get(index).toInt)
      .map(i => lines.get(index + i).split("\t"))
      .map(split => split(0).toInt -> (split(1), split(2).toLong))
      .toMap

    GraphHeader(options,
      genomeData.mapValues(_._1),
      genomeData.mapValues(_._2))
  }

}
