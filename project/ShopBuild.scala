import play.PlayImport._
import play.PlayScala
import sbt.Keys._
import sbt._

object ShopBuild extends Build {
  val slickVersion = "3.1.0"

  lazy val mainProject = Project(
    id = "shop_restful",
    base = file("."),
    settings = super.settings ++ Seq(
      resolvers ++= Seq("scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"),
      scalaVersion := "2.11.6",
      libraryDependencies ++= Seq(
        jdbc,
        cache,
        ws,
        filters,
        "com.softwaremill.macwire" %% "macros" % "1.0.1",
        "org.mockito" % "mockito-core" % "1.10.19" % "test",
        "com.typesafe.play" %% "play" % "2.3.8" withSources(),
        "org.postgresql" % "postgresql" % "9.4-1201-jdbc4",
        "org.apache.commons" % "commons-dbcp2" % "2.1",
        "com.typesafe" % "config" % "1.3.0",
        "com.typesafe.slick" %% "slick" % "3.1.0",
        "com.typesafe.slick" %% "slick-codegen" % "3.1.0"
      ),
      slick <<= slickCodeGenTask // register manual sbt command
      //sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use
    )).enablePlugins(PlayScala)

  // code generation task

  lazy val slick = taskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (baseDirectory, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "app").getPath // place generated files in sbt's managed sources folder
  val url = "jdbc:postgresql://127.0.0.1/shop"
  val jdbcDriver = "org.postgresql.Driver"
    val slickDriver = "slick.driver.PostgresDriver"
    val pkg = "slick.generated"
    toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
    val fname = outputDir + "/slick/generated/Tables.scala"
    Seq(file(fname))
  }
}