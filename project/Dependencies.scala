import sbt._

// format: off

object Version {
  final val Scala     = "2.11.8"
  final val ScalaTest = "3.0.0"
  final val Circe     = "0.5.4"
  final val Finch     = "0.11.0-M4"
}

object Library {
  val circeCore      = "io.circe"           %% "circe-core"    % Version.Circe
  val circeGeneric   = "io.circe"           %% "circe-generic" % Version.Circe
  val circeParser    = "io.circe"           %% "circe-parser"  % Version.Circe
  val finchCirce     = "com.github.finagle" %% "finch-circe"   % Version.Finch
  val finchCore      = "com.github.finagle" %% "finch-core"    % Version.Finch
  val scalameta      = "org.scalameta"      %% "scalameta"     % "1.1.0"
  val scalaTest      = "org.scalatest"      %% "scalatest"     % Version.ScalaTest
  val paradisePlugin = "org.scalameta"      %  "paradise"      % "3.0.0-M5" cross CrossVersion.full
}

object Dependencies {
  import Library._

  val metaServices = Seq(circeCore, circeGeneric, circeParser, finchCirce, finchCore)
  val annotations = Seq(scalameta)
}
