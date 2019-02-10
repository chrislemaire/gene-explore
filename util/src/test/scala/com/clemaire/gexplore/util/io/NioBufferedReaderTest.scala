//package com.clemaire.gexplore.util.io
//
//import java.io.{BufferedReader, FileReader, Reader}
//import java.nio.file.{Path, Paths}
//
//import org.scalatest.{BeforeAndAfter, FunSuite}
//import org.scalatest.Matchers._
//
//class NioBufferedReaderTest
//  extends FunSuite
//    with BeforeAndAfter {
//
//  private implicit def pathFromResource(resource: String): Path =
//    Paths.get(Thread.currentThread.getContextClassLoader.getResource(resource).toURI)
//
//  private val smallLorem: Path = "test/io/small_lorem.txt"
//  private val smallLoremWoEol: Path = "test/io/small_lorem_wo_eol.txt"
//  private val fullLorem: Path = "test/io/full_lorem.txt"
//
//  private var underTest: NioBufferedReader = _
//  private var reference: BufferedReader = _
//
//  private def closeIfExists(reader: Reader): Unit =
//    if (reader != null)
//      try {
//        reader.close()
//      } catch {
//        case e: Exception =>
//          println(e)
//      }
//
//  test("readLine should work for single line file") {
//    underTest = new NioBufferedReader(smallLorem)
//
//    underTest.readLine should be("Lorem ipsum dolor sit amet")
//  }
//
//  test("readLine should work for single line file w/o EOL at EOF") {
//    underTest = new NioBufferedReader(smallLoremWoEol)
//
//    underTest.readLine should be("Lorem ipsum dolor sit amet")
//  }
//
//  test("readLine should work for multi-line lorem ipsum with buffSz=3") {
//    underTest = new NioBufferedReader(fullLorem, 3)
//    reference = new BufferedReader(new FileReader(fullLorem.toFile))
//
//    var refLine = reference.readLine()
//    while (refLine != null) {
//      underTest.readLine should be(refLine)
//      refLine = reference.readLine()
//    }
//  }
//
//  test("readLine should work for multi-line lorem ipsum with buffSz=8192") {
//    underTest = new NioBufferedReader(fullLorem, 8192)
//    reference = new BufferedReader(new FileReader(fullLorem.toFile))
//
//    var refLine = reference.readLine()
//    while (refLine != null) {
//      underTest.readLine should be(refLine)
//      refLine = reference.readLine()
//    }
//  }
//
//  test("read should work for a few bytes") {
//    underTest = new NioBufferedReader(fullLorem)
//    reference = new BufferedReader(new FileReader(fullLorem.toFile))
//
//    val refBuff = Array.ofDim[Char](82)
//    val testBuff = Array.ofDim[Char](82)
//
//    reference.read(refBuff)
//    underTest.read(testBuff, 0, 82)
//
//    testBuff should be(refBuff)
//  }
//
//  test("read should work for a lot of bytes") {
//    underTest = new NioBufferedReader(fullLorem)
//    reference = new BufferedReader(new FileReader(fullLorem.toFile))
//
//    val refBuff = Array.ofDim[Char](822)
//    val testBuff = Array.ofDim[Char](822)
//
//    reference.read(refBuff)
//    underTest.read(testBuff, 0, 822)
//
//    testBuff should be(refBuff)
//  }
//
//  test("position should start at 0") {
//    underTest = new NioBufferedReader(fullLorem)
//
//    underTest.position should be(0)
//  }
//
//  test("position should increment with read") {
//    underTest = new NioBufferedReader(fullLorem)
//
//    underTest.read(Array.ofDim[Char](180), 0, 180)
//
//    underTest.position should be(180)
//  }
//
//  test("position should increment with readLine") {
//    underTest = new NioBufferedReader(smallLorem)
//
//    underTest.readLine
//
//    underTest.position should be(28)
//  }
//
//  after {
//    closeIfExists(underTest)
//    closeIfExists(reference)
//
//    underTest = null
//    reference = null
//  }
//
//}
