import xerial.sbt.Sonatype._

publishMavenStyle := true

sonatypeProfileName := "org.carbonateresearch"
sonatypeProjectHosting := Some(GitHubHosting(user="cedricmjohn", repository="conus", email="cedric.john@gmail.com"))
developers := List(
  Developer(id = "cedric", name = "Cedric M. John", email = "cedric.john@gmail.com", url = url("http://www.carbonateresearch.org"))
)
licenses := Seq("GNU GPL 3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html"))

publishTo := sonatypePublishToBundle.value
