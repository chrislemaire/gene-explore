package com.clemaire.gexplore.core.gfa.cache

import java.nio.file.Paths

import com.clemaire.gexplore.core.gfa.CachePathList
import com.clemaire.gexplore.core.gfa.reference.cache.SimpleReferenceBuilder
import com.clemaire.gexplore.core.gfa.reference.parsing.BufferedGfa1Parser
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.HashSet

class SimpleReferenceBuilderTest
  extends FunSuite
    with BeforeAndAfter {

//  private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\TB10.gfa"))
//  private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\chr19.hg38.w115.gfa"))
  private val paths = new CachePathList(Paths.get("C:\\Users\\Chris Lemaire\\Documents\\Delft\\Bachelor Computer Science year 2\\Q4\\[TI2806] - Context Project\\data\\Tomato_150_VCFs_2.50_chr06.gfa"))

  private var underTest: SimpleReferenceBuilder = _

  before {
    underTest = new SimpleReferenceBuilder(paths)
  }

  test("Test TB10") {
    underTest.buildWith(new BufferedGfa1Parser(HashSet("ORI")))
  }

  after {
    underTest.close()
  }

}
