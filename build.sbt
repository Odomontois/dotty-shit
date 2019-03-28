val dottyVersion = "0.13.0-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-simple",
    version := "0.1.0",

    scalaVersion := dottyVersion,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    libraryDependencies += ("org.typelevel" %% "cats-core" % "1.5.0").withDottyCompat(dottyVersion),
    libraryDependencies += ("org.typelevel" %% "cats-effect" % "1.2.0").withDottyCompat(dottyVersion),

    scalacOptions ++= List("-Xmax-inlines", "300")
  )
