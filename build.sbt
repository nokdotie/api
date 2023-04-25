val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "api",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.0-RC1",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
