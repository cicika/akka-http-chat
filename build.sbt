val akkaVersion = "2.4.16"
val akkaHttpVersion = "10.0.1"
val phantomVersion = "2.0.0"

lazy val root = (project in file("."))
  .settings(
    name         := "akka-http-chat",
    organization := "info.cicika",
    scalaVersion := "2.12.1",
    version      := "1.0-SNAPSHOT",
    libraryDependencies ++= dependecySettings
  )

  lazy val dependecySettings = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion % "compile",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion % "compile",
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion % "compile",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion % "compile",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % "compile",
    "com.outworkers" %% "phantom-dsl" % phantomVersion % "compile",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )
