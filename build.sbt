ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "imagy",
    idePackagePrefix := Some("me.katze.imagy")
  )

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.2"
libraryDependencies += "io.github.iltotore" %% "iron" % "2.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.17"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % "test"
