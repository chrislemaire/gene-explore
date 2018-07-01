package com.clemaire.gexplore.core.gfa.reference.parsing

import java.nio.file.Files

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.Gfa1Parser

class BufferedGfa1Parser(tags: Set[String])
  extends Gfa1Parser(tags) {

  override def parse(paths: CachePathList): Unit = {
    var offset = 0

    Files.lines(paths.gfaPath).forEach(line => {
      parseLine(line, offset)
      offset += line.length + 1
    })
  }

}
