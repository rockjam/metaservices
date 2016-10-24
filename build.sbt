lazy val `try-meta` = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

libraryDependencies ++= Vector(
  Library.scalaTest % "test"
)

initialCommands := """|import com.github.rockjam.try.meta._
                      |""".stripMargin
