organization := "as"

name := "makeSheet"

version := "0.1"

libraryDependencies += "org.rogach" %% "scallop" % "0.6.2"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)
mainClass in oneJar := Some("makeSheet")
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

