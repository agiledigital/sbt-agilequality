pomIncludeRepository := { _ =>
  false
}

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/agiledigital/sbt-agilequality</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/agiledigital/sbt-agilequality</connection>
      <developerConnection>scm:git:git@github.com:agiledigital/sbt-agilequality</developerConnection>
      <url>github.com/agiledigital/sbt-agilequality</url>
    </scm>
    <developers>
      <developer>
        <id>jjsmith@agiledigital.com.au</id>
        <name>Justin Smith</name>
      </developer>
    </developers>
}
