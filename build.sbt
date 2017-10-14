name := """sbt-agilequality"""
organization := "au.com.agiledigital"
sbtPlugin := true
crossSbtVersions := Seq("0.13.16", "1.0.2")

addSbtPluginHack("org.wartremover" % "sbt-wartremover" % "2.2.1")
addSbtPluginHack("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPluginHack("com.lucidchart" % "sbt-scalafmt" % "1.12")

/**
  * Workaround for https://github.com/sbt/sbt/issues/3393.
  */
def addSbtPluginHack(dependency: ModuleID): Setting[Seq[ModuleID]] =
  libraryDependencies += {
    val sbtV = (sbtBinaryVersion in pluginCrossBuild).value
    val scalaV = (scalaBinaryVersion in update).value
    Defaults.sbtPluginExtra(dependency, sbtV, scalaV)
  }

// http://www.scala-sbt.org/0.13/docs/Testing-sbt-plugins.html
ScriptedPlugin.scriptedSettings
scriptedLaunchOpts := {
  scriptedLaunchOpts.value ++
    Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + (version in ThisBuild).value)
}
