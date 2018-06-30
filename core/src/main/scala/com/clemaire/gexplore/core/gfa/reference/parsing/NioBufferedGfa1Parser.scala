package com.clemaire.gexplore.core.gfa.reference.parsing

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.Gfa1Parser
import com.clemaire.gexplore.util.io.NioBufferedReader

class NioBufferedGfa1Parser
  extends Gfa1Parser {

  override def parse(paths: CachePathList): Unit = {
    val reader = new NioBufferedReader(paths.gfaPath)
    var lastOffset = 0L

    try {
      reader.lines()
        .foreach(line => {
          parseLine(line, lastOffset)
          lastOffset = reader.position
        })
    } finally {
      reader.close()
    }
  }
}
