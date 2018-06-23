package com.clemaire.gexplorer.core.gfa.reference.parsing

import java.io._

import com.clemaire.gexplorer.core.gfa.CachePathList
import com.clemaire.gexplorer.core.gfa.reference.Gfa1Parser

class BufferedGfa1Parser
  extends Gfa1Parser {

  override def parse(paths: CachePathList): Unit = {
    var offset = 0

    var readTime = 0L
    var writeTime = 0L

    val stream = new BufferedReader(new FileReader(paths.gfaPath.toFile))
      .lines()

    var refTime = System.currentTimeMillis()

    stream.forEach(line => {
      readTime += System.currentTimeMillis() - refTime
      refTime = System.currentTimeMillis()

      parseLine(line, offset)
      offset += line.length + 1

      writeTime += System.currentTimeMillis() - refTime
      refTime = System.currentTimeMillis()
    })

    println(s"Read: $readTime, Write: $writeTime")
  }

}
