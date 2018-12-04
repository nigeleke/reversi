lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.nigeleke",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),

    name := "reversi",

    libraryDependencies ++= Seq(
      "org.mockito" % "mockito-core" % "2.23.4" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
  )
