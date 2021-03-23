name := "dispatcher-sdk"

organization := "ot.dispatcher"

version := "1.1.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"

libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"

credentials += Credentials("Sonatype Nexus Repository Manager", "192.168.4.195", sys.env.get("NEXUS_COMMON_CREDS_USR").getOrElse(""), sys.env.get("NEXUS_COMMON_CREDS_PSW").getOrElse(""))

publishTo := {
  val nexus = sys.env.get("NEXUS_OTP_URL").getOrElse("")
  if (isSnapshot.value)
    Some("snapshots" at nexus + "/repository/sbt-snapshots")
  else
    Some("releases"  at nexus + "/repository/sbt-releases")
}


