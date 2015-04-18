import sbt._

val common = Seq(
  name := "json4s-ext",
  organization := "com.adlawson",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.5",
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xmax-classfile-name", "127",
    "-Ywarn-unused-import"),
  libraryDependencies ++= Seq(
    "org.json4s"    %% "json4s-native" % "3.2.11",
    "org.scalatest" %% "scalatest"     % "2.1.6"   % "test"))

lazy val json4s = (project in file("."))
  .settings(common: _*)

lazy val examples = project
  .dependsOn(json4s)
  .settings(common: _*)
  .settings(name := "json4s-examples")
