package com.clemaire.gexplore.core.gfa.reference.parsing

import java.nio.file.Files

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.Gfa1Parser

class BufferedGfa1Parser
  extends Gfa1Parser {

  override def parse(paths: CachePathList): Unit = {
    var offset = 0

    val stream = Files.lines(paths.gfaPath)

    stream.forEach(line => {
      parseLine(line, offset)
      offset += line.length + 1
    })
  }

}
