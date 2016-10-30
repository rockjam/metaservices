lazy val metaSettigns = Seq(
  addCompilerPlugin(Library.paradisePlugin),
  scalacOptions += "-Xplugin-require:macroparadise")

lazy val metaServices = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(metaSettigns)
  .settings(libraryDependencies ++= Dependencies.metaServices)
  .dependsOn(annotations)

lazy val annotations = project
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)
  .settings(libraryDependencies ++= Dependencies.annotations)
  .settings(metaSettigns)

libraryDependencies ++= Vector(
  Library.scalaTest % "test"
)

initialCommands := """|import com.github.rockjam.metaservices._
                      |""".stripMargin
