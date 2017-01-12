organization := "channing"

name := "doobie-play"

version := "0.0.1"

scalaVersion := "2.11.8"

scalaBinaryVersion := "2.11"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture")

libraryDependencies += "org.tpolecat" %% "doobie-core-cats" % "0.3.1-M1"

libraryDependencies += "org.typelevel" %% "cats" % "0.7.2"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"

libraryDependencies += "com.h2database" % "h2" % "1.3.176"