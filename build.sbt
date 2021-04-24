organization := "org.imagination"

name := "Comparator"

version := "1.2"

scalaVersion := "2.13.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "UTF-8")

crossScalaVersions := Seq("2.13.5")

description := """a json/xml tree assertion tool """

licenses := Seq("The Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/ximagination80/Comparator"))

val fastXMLVersion =  "2.12.3"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % fastXMLVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % fastXMLVersion,
  "com.google.code.gson" % "gson" % "2.8.6"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.13" % "3.2.7"
).map(_ % Test)

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