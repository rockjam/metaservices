lazy val metaServices = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(scalametaSettigns)
  .settings(libraryDependencies ++= Dependencies.metaServices)
  .dependsOn(annotations)

lazy val annotations = project
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(libraryDependencies ++= Dependencies.annotations)
  .settings(scalametaSettigns)

libraryDependencies ++= Vector(
  Library.scalaTest % "test"
)

lazy val scalametaSettigns = Seq(
  addCompilerPlugin(Library.paradisePlugin),
  scalacOptions += "-Xplugin-require:macroparadise")

initialCommands := """|import com.github.rockjam.metaservices._
                      |""".stripMargin
