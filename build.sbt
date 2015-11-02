name := "Comparator"

version := "0.2-SNAPSHOT"

organization := "imagination"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

coverageEnabled := true

coverageMinimum := 90

coverageFailOnMinimum := true

parallelExecution in Test := false

publishMavenStyle := true

publishArtifact in Test := false