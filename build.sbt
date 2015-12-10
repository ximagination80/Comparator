name := "Comparator"

version := "0.3-SNAPSHOT"

organization := "imagination"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "UTF-8")

crossScalaVersions := Seq("2.11.7","2.10.5")

description := """a json/xml tree assertion tool """

licenses := Seq("The Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/ximagination80/Comparator"))

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

coverageEnabled.in(Test, test) := true

coverageMinimum := 90

coverageFailOnMinimum := true

parallelExecution in Test := false