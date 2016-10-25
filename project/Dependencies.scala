import sbt._

// format: off

object Version {
  final val Scala     = "2.11.8"
  final val ScalaTest = "3.0.0"
  final val Circe     = "0.5.4"
}

object Library {
  val scalaTest    = "org.scalatest" %% "scalatest"     % Version.ScalaTest
  val scalameta    = "org.scalameta" %% "scalameta"     % "1.1.0"
  val circeCore    = "io.circe"      %% "circe-core"    % Version.Circe
  val circeGeneric = "io.circe"      %% "circe-generic" % Version.Circe
  val circeParser  = "io.circe"      %% "circe-parser"  % Version.Circe
}

object Dependencies {
  import Library._

  val tryMeta = Seq(circeCore, circeGeneric, circeParser)
  val annotations = Seq(scalameta)
}
