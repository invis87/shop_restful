name := """shop_restful"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
//  ,
//  "Kaliber Repository" at "https://jars.kaliber.io/artifactory/libs-release-local"
)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "com.softwaremill.macwire" %% "macros" % "1.0.1",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "com.typesafe.play" %% "play" % "2.3.8" withSources()
)

//todo: compile time DI?
// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
