package com.clemaire.cache

import java.nio.file.{Path, Paths}

import com.clemaire.cache.definitions.{Cache, Identifiable}
import com.clemaire.cache.definitions.index.ChunkIndex
import com.clemaire.cache.definitions.io.reading.DataReader
import com.clemaire.cache.impl.BasicCache
import com.clemaire.cache.impl.io.reading.BasicChunkReader
import com.clemaire.cache.impl.io.writing.{BasicChunkWriter, BasicDataWriter}
import com.clemaire.io.fixture.OutputFixture
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.util.Random

class BasicCacheTest
  extends FunSuite
    with BeforeAndAfter {

  private case class DummyData(id: Int, data: Int)
    extends Identifiable

  private val dataReader: DataReader[DummyData] =
    in => DummyData(in.getInt, in.getInt)

  private val dataWriter: BasicDataWriter[DummyData] = new BasicDataWriter[DummyData](8) {
    override def writeData(data: DummyData, out: OutputFixture): Unit = {
      out.writeInt(data.id)
      out.writeInt(data.data)
    }
  }

  private trait AdjustedMax[D <: Identifiable] extends BasicCache[D] {
    override val max: Int = 3
  }

  private val dataPath: Path = Paths.get("./test-dummy-data.data")
  private val indexPath: Path = Paths.get("./test-dummy-data.index")

  private var testCache: Cache[DummyData, ChunkIndex] = _

  before {
    testCache = new BasicCache(
      new BasicChunkWriter[DummyData](dataPath, dataWriter),
      new BasicChunkReader[DummyData, ChunkIndex](dataPath, dataReader),
      indexPath
    ) with AdjustedMax[DummyData]
  }

  after {
    testCache.close()
  }

  test("Write data") {
    (0 to 8191).foreach { i =>
      testCache += DummyData(i, Random.nextInt(1000))
    }

    val testROCache = testCache.finish

    println(testROCache.getRange(0, 3))
  }

}
