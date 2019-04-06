package com.clemaire.gexplore.core.gfa.parsing

import java.nio.file.Files

import com.clemaire.gexplore.core.gfa.CachePathList

class BufferedGfa1Parser(tags: Set[String])
  extends Gfa1Parser(tags) {

  override def parse(paths: CachePathList): Unit = {
    var offset = 0

    val reader = Files.newBufferedReader(paths.gfaPath)
    var line = reader.readLine()
    while (line != null) {
      parseLine(line, offset)
      offset += line.length + 1

      line = reader.readLine()
    }
  }

}
