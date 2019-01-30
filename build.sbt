name := "gexplore"
version := "0.1"
scalaVersion := "2.12.6"

lazy val root = project.in(file("."))
  .aggregate(core, ui, util)

lazy val core = project
  .settings(
    name := "core",
    libraryDependencies ++= commonDependencies ++ Set(
      dependencies.intervalTree
    ))
  .dependsOn(util)

lazy val ui = project
  .settings(
    name := "ui",
    libraryDependencies ++= commonDependencies ++ Set(
      dependencies.miglayout,
      dependencies.jfoenix,

      dependencies.ikonli,
      dependencies.ikonliDashicons
    ))

lazy val util = project
  .settings(
    name := "util",
    libraryDependencies ++= commonDependencies)

lazy val dependencies = new {
  val scalatestV = "3.0.4"
  val scalacheckV = "1.13.5"
  val intervalTreeV = "1.0.0"
  val miglayoutV = "5.2"
  val jfoenixV = "8.0.8"
  val ikonliV = "2.4.0"

  val scalatest = "org.scalatest" %% "scalatest" % scalatestV
  val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
  val intervalTree = "com.lodborg" % "interval-tree" % intervalTreeV
  val miglayout = "com.miglayout" % "miglayout-javafx" % miglayoutV
  val jfoenix = "com.jfoenix" % "jfoenix" % jfoenixV

  val ikonli = "org.kordamp.ikonli" % "ikonli-javafx" % ikonliV
  val ikonliDashicons = "org.kordamp.ikonli" % "ikonli-dashicons-pack" % ikonliV
}

lazy val commonDependencies = Seq(
  dependencies.scalatest,
  dependencies.scalacheck
)
