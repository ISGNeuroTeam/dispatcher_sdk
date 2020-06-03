name := "dispatcher-sdk"

organization := "ot.dispatcher"

version := "1.0.0"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"

libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"
