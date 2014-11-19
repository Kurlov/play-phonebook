import play.Project._
import NativePackagerKeys._

name := "PhoneBook"

version := "1.0"

packageArchetype.java_application

scalaVersion := "2.10.2"

playScalaSettings

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.twitter" % "finagle-http_2.10" % "6.18.0",
  "postgresql" % "postgresql" % "9.0-801.jdbc4"
)