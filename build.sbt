val scala3Version = "3.3.1"

Universal / javaOptions ++= Seq(
  "-J-Xms4G",
  "-J-Xmx4G"
)

Universal / scalacOptions ++= Seq(
  "-Xms4G",
  "-Xmx4G"
)

lazy val root = project
  .in(file("."))
  .settings(
    name         := "api",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    resolvers += Resolver.githubPackages("nokdotie"),
    libraryDependencies ++= Seq(
      "com.google.firebase"          % "firebase-admin"   % "9.1.1",
      "com.github.ghostdogpr"       %% "caliban"          % "2.1.0",
      "com.github.ghostdogpr"       %% "caliban-zio-http" % "2.1.0",
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio"   % "1.5.4",
      "com.stripe"                   % "stripe-java"      % "22.18.0",
      "dev.zio"                     %% "zio-http"         % "0.0.5",
      "ch.qos.logback"               % "logback-classic"  % "1.4.14", // TODO add logger config
      "dev.zio"                     %% "zio-test"         % "2.0.15" % Test,
      "dev.zio"                     %% "zio-test-sbt"     % "2.0.15" % Test,
      "ie.nok"                      %% "adverts"          % "20240307.092232.980759883"
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
