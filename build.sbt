organization := "channing"

name := "doobie-play"

version := "0.0.1"

scalaVersion := "2.12.1"

scalaBinaryVersion := "2.12"

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
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture")

libraryDependencies += "org.tpolecat" %% "doobie-core-cats" % "0.4.1"

libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"

libraryDependencies += "com.h2database" % "h2" % "1.4.193"