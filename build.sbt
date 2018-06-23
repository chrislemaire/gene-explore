name := "gexplore"
version := "0.1"
scalaVersion := "2.12.6"

lazy val root = project.in(file("."))
  .aggregate(core, ui, util)

lazy val core = project
  .settings(
    name := "core",
    libraryDependencies ++= commonDependencies)
  .dependsOn(util)

lazy val ui = project
  .settings(
    name := "ui",
    libraryDependencies ++= commonDependencies)

lazy val util = project
  .settings(
    name := "ui",
    libraryDependencies ++= commonDependencies)

lazy val dependencies = new {
  val scalatestV = "3.0.4"
  val scalacheckV = "1.13.5"

  val scalatest = "org.scalatest" %% "scalatest" % scalatestV
  val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
}

lazy val commonDependencies = Seq(
  dependencies.scalatest,
  dependencies.scalacheck
)
