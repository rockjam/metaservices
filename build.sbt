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
  scalacOptions += "-Xplugin-require:macroparadise",
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns),
  scalacOptions in (Compile, console) += "-Yrepl-class-based" // necessary to use console
)

initialCommands := """|import com.github.rockjam.metaservices._
                      |""".stripMargin
