organization := "org.imagination"

name := "Comparator"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "UTF-8")

crossScalaVersions := Seq("2.11.7","2.10.5")

description := """a json/xml tree assertion tool """

licenses := Seq("The Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/ximagination80/Comparator"))

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4"
).map(_ % Test)

coverageEnabled.in(Test, test) := true

coverageMinimum := 95

coverageFailOnMinimum := true

parallelExecution in Test := false

publishMavenStyle := true

publishTo :=
  Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}