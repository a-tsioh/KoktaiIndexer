name := "KoktaiIndexer"

version := "0.1"

scalaVersion := "2.12.6"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "fr.magistry.koktai"
)

lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    guice,
    "org.scala-lang.modules" %% "scala-xml" % "1.1.0",
    "com.lihaoyi" %% "ujson" % "0.6.6",
    "com.lihaoyi" %% "upickle" % "0.4.4",
    //"org.elasticsearch" % "elasticsearch" % "2.3.2",
    "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "6.3.1",
    "org.webjars.bower" % "jquery" % "3.2.1",
    "org.webjars.bower" % "semantic-ui" % "2.2.13",
    "io.suzaku" %% "boopickle" % "1.3.0",
    "com.lihaoyi" %% "autowire" % "0.2.6",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  ),
  ).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  //resolvers += Resolver.jcenterRepo,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5",
    "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "6.3.1",
    //"com.lihaoyi" %%% "upickle" % "0.6.6",
    "io.suzaku" %%% "boopickle" % "1.3.0",
    "com.lihaoyi" %%% "ujson" % "0.6.6",
    "com.lihaoyi" %%% "scalatags" % "0.6.7",
    "com.lihaoyi" %%% "autowire" % "0.2.6",
    //"com.lihaoyi" %%% "upickle" % "0.4.4",
    //"org.scala-lang.modules" %%% "scala-xml" % "1.1.0",
    "io.suzaku" %%% "diode-core" % "1.1.3"
    //"com.definitelyscala" %%% "scala-js-yamljs" % "1.1.0"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}

