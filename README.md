# AgileQuality

An opinionated code quality plugin. Aggregates [sbt-wartremover](http://www.wartremover.org/), [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) and [sbt-scalafmt](https://github.com/lucidsoftware/neo-sbt-scalafmt). 

## Usage

Add the following to your `plugins.sbt`:

```
  addSbtPlugin("au.com.agiledigital" % "sbt-agilequality" % "0.1-SNAPSHOT")
```

### Testing

Run `test` for regular unit tests.

Run `scripted` for [sbt script tests](http://www.scala-sbt.org/0.13/docs/Testing-sbt-plugins.html).

### Publishing

Run `^publishLocal` to publish locally. Ensure you include the `^` prefix to publish for both sbt 0.13.x and 1.0.x.