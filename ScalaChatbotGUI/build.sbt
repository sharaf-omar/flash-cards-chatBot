name := "ScalaChatbotGUI"

version := "0.1"

scalaVersion := "3.2.2"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "20.0.0-R31",
  "com.github.tototoshi" %% "scala-csv" % "1.3.10"
)

Compile / resourceDirectory := baseDirectory.value / "src" / "main" / "resources"

