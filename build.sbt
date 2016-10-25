lazy val metaSettigns = Seq(
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M5" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise")

lazy val `try-meta` = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(metaSettigns)
  .dependsOn(annotations)

lazy val annotations = project
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(
    libraryDependencies += Library.scalameta
  )
  .settings(metaSettigns)

libraryDependencies ++= Vector(
  Library.scalaTest % "test"
)

initialCommands := """|import com.github.rockjam.try.meta._
                      |""".stripMargin
