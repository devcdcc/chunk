ThisBuild / version      := "0.1.0-SNAPSHOT"ThisBuild / scalaVersion := "3.2.2"ThisBuild / organization := "chunk"lazy val root = (project in file("."))  .settings(name := "chunk")// zio librarieslibraryDependencies += "dev.zio"     %% "zio"          % "2.0.13"libraryDependencies += "dev.zio"     %% "zio-streams"  % "2.0.13"libraryDependencies += "dev.zio"     %% "zio-test-sbt" % "2.0.13"libraryDependencies += "dev.zio"     %% "zio-prelude"  % "1.0.0-RC19"// parsinglibraryDependencies += "com.lihaoyi" %% "fastparse"    % "3.0.1"ThisBuild / idePackagePrefix := Some("chunk")