package au.com.agiledigital.agilequality

import com.lucidchart.sbt.scalafmt.ScalafmtPlugin
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import sbt._
import sbt.Keys._
import wartremover.WartRemover
import wartremover.WartRemover.autoImport.{Wart, wartremoverWarnings}
import scoverage.ScoverageSbtPlugin

object AgileQualityPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = {
    WartRemover && ScoverageSbtPlugin && ScalafmtPlugin
  }

  override lazy val projectSettings: Seq[Setting[_]] = {
    javacSettings ++ scalacSettings ++ wartRemoverSettings ++ scalaFmtSettings ++ scoverageSettings
  }

  private lazy val javacSettings = Seq(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-encoding", "UTF-8")
  )

  // See https://tpolecat.github.io/2017/04/25/scalac-flags.html
  private lazy val scalacSettings = Seq(
    scalacOptions ++= Seq(
      "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                // Specify character encoding used by source files.
      "-explaintypes",                     // Explain type errors in more detail.
      "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
      "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
      "-Xfuture",                          // Turn on future language features.
      "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
      "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
      "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
      "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
      "-Xlint:option-implicit",            // Option.apply used implicit view.
      "-Xlint:package-object-classes",     // Class or object defined in package object.
      "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
      "-Xlint:unsound-match",              // Pattern match may not be typesafe.
      "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
//      "-Ywarn-dead-code",                  // Warn when dead code is identified.
      "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
      "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
      "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
      "-Ywarn-numeric-widen",              // Warn when numerics are widened.
//      "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
//      "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
      "-Ywarn-unused:locals",              // Warn if a local definition is unused.
//      "-Ywarn-unused:params",              // Warn if a value parameter is unused.
//      "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
      "-Ywarn-unused:privates",            // Warn if a private member is unused.
      "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
    ),
    // Note that the REPL can’t really cope with -Ywarn-unused:imports or -Xfatal-warnings so you should turn them off for the console.
    scalacOptions in (Compile, console) ~= (_.filterNot(Set(
      "-Ywarn-unused:imports",
      "-Xfatal-warnings"
    ))),
    scalacOptions ++= Seq(
      // Workaround for https://github.com/playframework/playframework/issues/7382
      "-Ywarn-unused:-imports"
    )
  )

  // See http://www.wartremover.org/doc/warts.html
  private lazy val wartRemoverSettings = Seq(
    wartremoverWarnings in (Compile, compile) ++= Seq(
//      Wart.Any,
      Wart.ArrayEquals,
      Wart.AsInstanceOf,
      Wart.EitherProjectionPartial,
      Wart.Enumeration,
      Wart.Equals,
      Wart.ExplicitImplicitTypes,
      Wart.FinalCaseClass,
      Wart.FinalVal,
      Wart.IsInstanceOf,
      Wart.JavaConversions,
      Wart.LeakingSealed,
      Wart.MutableDataStructures,
      Wart.Nothing,
      Wart.Null,
      Wart.OptionPartial,
      Wart.Product,
      Wart.PublicInference,
      Wart.Recursion,
      Wart.Return,
      Wart.Serializable,
      Wart.StringPlusAny,
      Wart.Throw,
      Wart.TraversableOps,
      Wart.TryPartial,
      Wart.Var,
      Wart.While
      // TODO: https://github.com/wartremover/wartremover-contrib
      // TODO: https://github.com/danielnixon/extrawarts ?
    )
  )

  // See https://github.com/lucidsoftware/neo-sbt-scalafmt#task-configuration
  private lazy val scalaFmtSettings = Seq(
    scalafmtConfig := new File(getClass.getResource("default.scalafmt.conf").toExternalForm),
    // TODO: Use scalafmt 1.3.0 once sbt-scalafmt recognises it and stops emitting warnings.
    scalafmtVersion := "1.2.0"
  )

  private val scoverageSettings = Seq(
    // TODO https://github.com/scoverage/sbt-scoverage
  )
}