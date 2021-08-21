import ai.kien.python.Python

lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"
lazy val scala3   = "3.0.0"

ThisBuild / scalaVersion := scala213

def getProp(p: String) = Option(sys.props(p)).map(_.trim).filter(_.nonEmpty)

def getProps(prop: String*) =
  prop
    .map(p => p -> getProp(p))
    .collect { case (k, Some(v)) => s"""-D$k=$v""" }

lazy val scalapyVersion = getProp("plugin.scalapy.version").get

lazy val python = Python(getProp("plugin.python.executable"))

lazy val pythonLdFlags = python.ldflags.get

lazy val javaOpts = python.scalapyProperties.get.map { case (k, v) =>
  s"""-D$k=$v"""
}.toSeq

val checkModule = taskKey[Unit]("Check loading a dummy module")

lazy val root = crossProject(JVMPlatform, NativePlatform)
  .in(file("."))
  .settings(
    crossScalaVersions := Seq(scala212, scala213),
    Compile / mainClass := Some("project.Main")
  )
  .jvmSettings(
    fork := true,
    javaOptions ++= javaOpts ++ getProps("plugin.virtualenv"),
    libraryDependencies += "me.shadaj" %% "scalapy-core" % scalapyVersion,
    checkModule := {
      (Compile / runMain).toTask(" project.Module").value
    }
  )
  .nativeSettings(
    libraryDependencies += "me.shadaj" %%% "scalapy-core" % scalapyVersion,
    nativeLinkStubs := true,
    nativeLinkingOptions ++= pythonLdFlags
  )
