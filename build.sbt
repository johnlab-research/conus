val osName: SettingKey[String] = SettingKey[String]("osName")

osName := (System.getProperty("os.name") match {
  case name if name.startsWith("Linux") => "linux"
  case name if name.startsWith("Mac") => "mac"
  case name if name.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
})


version := "0.0.1"

scalaVersion:="2.13.1"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"

libraryDependencies += "io.monix" %% "monix" % "3.1.0-2156c0e"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "4.1.2",
  "org.apache.poi" % "poi-ooxml" % "4.1.2",
  "org.apache.poi" % "poi-ooxml-schemas" % "4.1.2"
)

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % "1.0",
  "org.scalanlp" %% "breeze-natives" % "1.0",
  "org.scalanlp" %% "breeze-viz" % "1.0"
)

libraryDependencies += "org.ojalgo" % "ojalgo" % "48.1.0"

libraryDependencies += "org.ujmp" % "ujmp-core" % "0.3.0"



