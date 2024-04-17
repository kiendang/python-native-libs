import sbt._

object Dependencies {
  lazy val scalaCollectionCompat =
    "org.scala-lang.modules" %% "scala-collection-compat" % "2.12.0"
  lazy val scalapy         = "me.shadaj"            %% "scalapy-core"     % "0.5.2"
  lazy val scalaTest       = "org.scalatest"        %% "scalatest"        % "3.2.11"
  lazy val organizeImports = "com.github.liancheng" %% "organize-imports" % "0.6.0"
}
