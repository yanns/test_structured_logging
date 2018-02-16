import sbt._

object Dependencies {
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  lazy val gCloudLogback = "com.google.cloud" % "google-cloud-logging-logback" % "0.34.0-alpha"
}
