package com.clemaire.gexplore.core.gfa.construction

import java.nio.file.Paths

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.parsing.BufferedGfa1Parser
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.HashSet

class GraphBuilderTest
  extends FunSuite
    with BeforeAndAfter {

  //    private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\TB10.gfa"))
  //  private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\chr19.hg38.w115.gfa"))
    private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\TB328v2.gfa"))
//  private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\Tomato_150_VCFs_2.50_chr06.gfa"))

  private var underTest: GraphBuilder = _

  before {
    underTest = new GraphBuilder(paths)
  }

  test("Test TB10") {
    val graph = underTest.buildWith(new BufferedGfa1Parser(HashSet("ORI")))
    val compGraph = new GraphReader(paths).readGraphData()
  }

  after {
    underTest.close()
  }

}
