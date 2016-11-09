import sbt._

// format: off

object Version {
  final val Scala     = "2.11.8"
  final val Scalameta = "1.3.0.522"
  final val Paradise  = {
    val latestPR = 121
    s"3.0.0.$latestPR"
  }
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
  val scalameta      = "org.scalameta"      %% "scalameta"     % Version.Scalameta
  val scalaTest      = "org.scalatest"      %% "scalatest"     % Version.ScalaTest
  val paradisePlugin = "org.scalameta"      %  "paradise"      % Version.Paradise cross CrossVersion.full
}

object Dependencies {
  import Library._

  val metaServices = Seq(circeCore, circeGeneric, circeParser, finchCirce, finchCore)
  val annotations = Seq(scalameta)
}
