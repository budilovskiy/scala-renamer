name := "renamer"

version := "1.0"

scalaVersion := "2.11.8"

val XMLdependencies: Seq[ModuleID] = Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
)

val configLib: Seq[ModuleID] = Seq(
  "com.typesafe" % "config" % "1.3.0"
)

libraryDependencies := XMLdependencies ++
  configLib