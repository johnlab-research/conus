val osName: SettingKey[String] = SettingKey[String]("osName")

osName := (System.getProperty("os.name") match {
  case name if name.startsWith("Linux") => "linux"
  case name if name.startsWith("Mac") => "mac"
  case name if name.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
})

version := "0.2.0"

scalaVersion:="2.13.2"
//crossScalaVersions := Seq(scalaVersion.value, "2.12.8","2.11.12")

resolvers += "jitpack" at "https://jitpack.io"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.5"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" //% Runtime

libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"

libraryDependencies += "com.github.jupyter" % "jvm-repr" % "0.4.0"

libraryDependencies += "sh.almond" %% "interpreter-api" % "0.10.0"

libraryDependencies += "sh.almond" %% "jupyter-api" % "0.10.0"

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

//libraryDependencies += "org.ojalgo" % "ojalgo" % "48.1.0"

val username = "cedricjohn"
val repo     = "conus"
//name := "conus"

inThisBuild(
  List(
    organization := "org.carbonateresearch",
    homepage := Some(url(s"https://github.com/$username/$repo")),
    licenses := List("GNU General Public License" -> url(s"https://github.com/$username/$repo/LICENSE")),
    developers := List(
      Developer(
        id = username,
        name = "Cedric John",
        email = "cedric.john@gmail.com",
        url = new URL(s"http://github.com/${username}")
      )
    )
  )
)


ThisBuild / description := "CoNuS is a library for Concurrent Numerical Simulations."
ThisBuild / licenses := List("GNU General Public License" -> new URL("http://www.gnu.org/licenses/"))
ThisBuild / homepage := Some(url("https://github.com/cedricmjohn/conus"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

publishTo := sonatypePublishToBundle.value



