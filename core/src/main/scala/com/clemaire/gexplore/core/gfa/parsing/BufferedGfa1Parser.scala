package com.clemaire.gexplore.core.gfa.parsing

import java.nio.file.Files

import com.clemaire.gexplore.core.gfa.CachePathList

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
