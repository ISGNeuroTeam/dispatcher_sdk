name := "dispatcher-sdk"

organization := "ot.dispatcher"

version := "2.0.0"

scalaVersion := "2.12.10"

ThisBuild / useCoursier := false

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.2"

libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10"

credentials += Credentials("Sonatype Nexus Repository Manager", sys.env.getOrElse("NEXUS_HOSTNAME", ""), sys.env.getOrElse("NEXUS_COMMON_CREDS_USR", ""), sys.env.getOrElse("NEXUS_COMMON_CREDS_PSW", ""))

publishTo := Some("Sonatype Nexus Repository Manager" at sys.env.getOrElse("NEXUS_OTP_URL_HTTPS", "") + "/repository/ot.platform-sbt-releases")
