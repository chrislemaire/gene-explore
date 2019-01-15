package com.clemaire.gexplore.util.io

import java.io.BufferedReader
import java.nio.file.{Files, Path, Paths}

import com.clemaire.gexplore.util.Stopwatch
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}

class NioBufferedReaderComparison
  extends FunSuite
    with BeforeAndAfter
    with BeforeAndAfterAll {

  private implicit def pathFromResource(resource: String): Path =
    Paths.get(Thread.currentThread.getContextClassLoader.getResource(resource).toURI)

  private val fullLorem: Path = "test/io/full_lorem_large.txt"

  private var nioBR: NioBufferedReader = new NioBufferedReader(fullLorem)
  private var brj: BufferedReader = Files.newBufferedReader(fullLorem)

  before {
    reset()

    Stopwatch.reset("niobr")
    Stopwatch.reset("brj")
  }

  after {
    close()
  }

  def close(): Unit = {
    nioBR.close()
    brj.close()
  }

  def reset(): Unit = {
    close()

    nioBR = new NioBufferedReader(fullLorem, 65536)
    brj = Files.newBufferedReader(fullLorem)
  }

  def testLorem(name: String): Unit = {
    var currLine: String = ""
    (1 to 200).foreach(_ => {
      Stopwatch.start("niobr")
      nioBR.lines().foreach(line => currLine = line)
      Stopwatch.stop("niobr")

      Stopwatch.start("brj")
      brj.lines().forEach(line => currLine = line)
      Stopwatch.stop("brj")

      reset()
    })

    println(s"$name:")
    println(s"NioBufferedReader: ${Stopwatch.timeFor("niobr")}")
    println(s"BufferedReader: ${Stopwatch.timeFor("brj")}\n")
  }

  def testByteData(name: String): Unit = {
    val BUFF_SIZE: Int = 8192

    var currBuff: Array[Char] = Array.ofDim(BUFF_SIZE)

    val len = fullLorem.toFile.length()
    val rounds = Math.floorDiv(len.toInt, BUFF_SIZE)

    (1 until 200).foreach(_ => {
      Stopwatch.start("niobr")
      for (_ <- 0 until rounds) {
        currBuff = Array.ofDim(BUFF_SIZE)
        nioBR.read(currBuff)
      }
      Stopwatch.stop("niobr")

      Stopwatch.start("brj")
      for (_ <- 0 until rounds) {
        currBuff = Array.ofDim(BUFF_SIZE)
        brj.read(currBuff)
      }
      Stopwatch.stop("brj")

      reset()
    })

    println(s"$name:")
    println(s"NioBufferedReader: ${Stopwatch.timeFor("niobr")}")
    println(s"BufferedReader: ${Stopwatch.timeFor("brj")}\n")
  }


  test("Warmup") {
    testLorem("Warmup")
  }

  test("Lines: run 1") {
    testLorem("Lines: run 1")
  }

  test("Lines: run 2") {
    testLorem("Lines: run 2")
  }

  test("Lines: run 3") {
    testLorem("Lines: run 3")
  }

  test("Bytes: run 1") {
    testByteData("Bytes: run 1")
  }

  test("Bytes: run 2") {
    testByteData("Bytes: run 2")
  }

  test("Bytes: run 3") {
    testByteData("Bytes: run 3")
  }

}
