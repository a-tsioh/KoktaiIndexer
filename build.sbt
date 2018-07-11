name := "KoktaiIndexer"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "com.lihaoyi" %% "ujson" % "0.6.6",
  "com.lihaoyi" %% "upickle" % "0.4.4",
  //"org.elasticsearch" % "elasticsearch" % "2.3.2",
  "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "6.3.1"
)