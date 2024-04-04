ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"
/*
lazy val root = (project in file("."))
  .settings(
    name := "imagy",
    idePackagePrefix := Some("me.katze.imagy")
  )*/

lazy val component = (project in file("./components"))
  .settings(
    name := "components",
    idePackagePrefix := Some("me.katze.imagy.components"),
    libraryDependencies := defaultLibraries
  ).dependsOn(common)

lazy val desktop = (project in file("./desktop"))
  .settings(
    name := "desktop",
    idePackagePrefix := Some("me.katze.imagy.desktop"),
    libraryDependencies := defaultLibraries
  ).dependsOn(common)

lazy val layout = (project in file("./layout"))
  .settings(
    name := "layout",
    idePackagePrefix := Some("me.katze.imagy.layout"),
    libraryDependencies := defaultLibraries
  ).dependsOn(common, component)

lazy val common = (project in file("./common"))
  .settings(
    name := "common",
    idePackagePrefix := Some("me.katze.imagy.common"),
    libraryDependencies := defaultLibraries
  )

lazy val example_application = (project in file("./example_application"))
  .settings(
    name := "example_application",
    idePackagePrefix := Some("me.katze.imagy.example"),
    libraryDependencies := defaultLibraries ++ effectLibaries ++ graphicLibraries,
  ).dependsOn(common, component, desktop, layout)

def defaultLibraries = Seq(
  "org.typelevel" %% "cats-mtl" % "1.3.0",
  "io.github.iltotore" %% "iron" % "2.4.0",
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test,
  "org.typelevel" %% "cats-effect" % "3.5.2" % Test,
  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)

def effectLibaries  = Seq(
  "org.typelevel" %% "cats-effect" % "3.5.2",
)

def lwjglVersion = "3.3.3"

def graphicLibraries : Seq[ModuleID] = Seq(
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
)