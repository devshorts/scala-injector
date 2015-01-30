name := "play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  jdbc ,
  anorm ,
  cache ,
  ws ,
  "org.sql2o" % "sql2o" % "1.5.0" ,
  "com.google.inject" % "guice" % "3.0" ,
  "javax.inject" % "javax.inject" % "1")



unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

unmanagedJars in Compile += file("app/lib/sqljdbc4.jar")

fork := true

javaOptions += "-Djava.library.path=app/lib"