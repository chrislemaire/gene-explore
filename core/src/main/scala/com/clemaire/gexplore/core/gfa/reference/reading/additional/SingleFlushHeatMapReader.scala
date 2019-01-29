package com.clemaire.gexplore.core.gfa.reference.reading.additional

import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.ByteBuffer

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.data.HeatMap.HeatMap

class SingleFlushHeatMapReader(val paths: CachePathList) {

  def readAll(): HeatMap = {
    val fc: FileChannel = FileChannel.open(paths.heatMapPath, StandardOpenOption.READ)
    val byteBuff: ByteBuffer = ByteBuffer.allocate(fc.size.toInt)

    fc.read(byteBuff)

    val result = byteBuff.asIntBuffer()
    (1 to result.capacity() / 2)
      .map(_ => (result.get, result.get))
      .toMap
  }

}
