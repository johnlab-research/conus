val osName: SettingKey[String] = SettingKey[String]("osName")

osName := (System.getProperty("os.name") match {
  case name if name.startsWith("Linux") => "linux"
  case name if name.startsWith("Mac") => "mac"
  case name if name.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
})

scalaVersion:="2.13.1"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

/*
libraryDependencies += "org.openjfx" % "javafx-base" % "11-ea+25" classifier osName.value

libraryDependencies += "org.openjfx" % "javafx-controls" % "11-ea+25" classifier osName.value

libraryDependencies += "org.openjfx" % "javafx-fxml" % "11-ea+25" classifier osName.value

libraryDependencies += "org.openjfx" % "javafx-graphics" % "11-ea+25" classifier osName.value

libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"

libraryDependencies += "org.scalafx" %% "scalafx-extras" % "0.3.0" */

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"

libraryDependencies += "io.monix" %% "monix" % "3.1.0-2156c0e"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "4.1.2",
  "org.apache.poi" % "poi-ooxml" % "4.1.2",
  "org.apache.poi" % "poi-ooxml-schemas" % "4.1.2"
)



