val scala3Version = "3.3.0"

Universal / javaOptions ++= Seq(
  "-J-Xms2G",
  "-J-Xmx2G"
)

Universal / scalacOptions ++= Seq(
  "-Xms2G",
  "-Xmx2G"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "api",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    resolvers += Resolver.githubPackages("nokdotie"),
    libraryDependencies ++= Seq(
      "com.google.firebase" % "firebase-admin" % "9.1.1",
      "com.github.ghostdogpr" %% "caliban" % "2.1.0",
      "com.github.ghostdogpr" %% "caliban-zio-http" % "2.1.0",
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % "1.5.4",
      "com.stripe" % "stripe-java" % "22.18.0",
      "dev.zio" %% "zio-http" % "0.0.5",
      "dev.zio" %% "zio-test" % "2.0.15" % Test,
      "dev.zio" %% "zio-test-sbt" % "2.0.15" % Test,
      "ie.nok" %% "adverts" % "20231128.161423.48242364",
      "ie.nok" %% "scala-libraries" % "20230911.141557.874954016"
    ),
    dockerRepository := Some("gcr.io/deed-ie"),
    dockerAliases ++= Seq(
      s"time-${Environment.instant}",
      s"sha-${Environment.gitShortSha1}"
    )
      .map(Option.apply)
      .map(dockerAlias.value.withTag),
    dockerExposedPorts ++= Seq(8080),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)
