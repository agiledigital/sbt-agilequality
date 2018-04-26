library 'play26-jenkins-library'

final project = "sbt-agilequality"

def volumes = play26BuilderPersistentVolumes(
  project: project
)

@NonCPS def uniqueBy(List items, String key) {
  return items.unique { a,b -> a[key] <=> b[key]}
}

def volumeClaims = uniqueBy(volumes, 'path').collect { volume ->
  persistentVolumeClaim(
    claimName: volume.claimName,
    mountPath: volume.path
  )
}

def containers = [
  [containerTemplate(
    name: 'jnlp',
    image: 'openshift/jenkins-slave-base-centos7:v3.7',
    args: '${computer.jnlpmac} ${computer.name}'
  )],
  play26BuilderContainerTemplate()
].flatten()

def buildNumber = env.BUILD_NUMBER;


final sbt = { cmd ->
  ansiColor('xterm') {
    // todo: configure these in a single place referred to in persistent volumes template //-Dsbt.repository.config=/home/jenkins/sbt.boot.properties
    sh "sbt -batch -sbt-dir /home/jenkins/.sbt -Dsbt.ivy.home=/home/jenkins/.ivy2/ -Divy.home=/home/jenkins/.ivy2/ -v \'${cmd}\'"
  }
}

final git = { cmd ->
  ansiColor('xterm') {
    sh "git ${cmd}"
  }
}

final versionPrefix = 'version in ThisBuild := "'
final versionSuffix = '"'
final versionRegex = /^((?:[0-9]+\.)+)([0-9]+)(?:-SNAPSHOT)?$/

final getVersion = { versionFile ->
  assert versionFile.startsWith(versionPrefix)
  assert versionFile.endsWith(versionSuffix)
  def versionString = versionFile.drop(versionPrefix.length()).reverse().drop(versionSuffix.length()).reverse()
  def match = (versionString =~ versionRegex)
  assert match.count == 1
  return [
    majorVersions: match[0][1],
    minorVersionNumber: match[0][2].toInteger()
  ]
}

podTemplate(label: "${project}-build-pod", cloud: 'openshift', containers: containers, volumes: volumeClaims) {
  node("${project}-build-pod") {

    try {
      stage('Checkout code') {
        checkout scm
      }
      container('play26-builder') {
        stage('Fetch dependencies') {
          sbt 'update'
        }
        stage('Run tests') {
          sbt 'test'
        }
        stage('Build') {
          sbt 'scripted'
        }


        if (env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == 'master') {
          def masterBranch = env.BRANCH_NAME == 'master'

          def currentVersion = getVersion(readFile('version.sbt'))
          def versionTag = masterBranch ? "" : "-${env.BUILD_NUMBER}"
          def newVersion = "${currentVersion.majorVersions}${currentVersion.minorVersionNumber}${versionTag}"
          writeFile('version.sbt', "${versionPrefix}${newVersion}${versionSuffix}")

          sbt "^publishSigned"

          if(masterBranch) {
            def nextVersion = "${currentVersion.majorVersions}${currentVersion.minorVersionNumber + 1}-SNAPSHOT"
            sbt "^sonatypePromote"
            writeFile('version.sbt', "${versionPrefix}${newVersion}${versionSuffix}")
            sshagent (credentials: ['github-push-credentials']) {
              git 'branch master'
              git 'add version.sbt'
              git "commit -m 'Automatic build number increment for release of ${newVersion}'"
              git 'push'
            }
          }
        }

      }
    } catch (InterruptedException e) {
      // Build interupted
      currentBuild.result = 'ABORTED'
      throw e
    } catch (e) {
      // If there was an exception thrown, the build failed
      currentBuild.result = 'FAILED'
      throw e
    } finally {
      // Success or failure, always send notifications
      //notifySlack(currentBuild.result)
    }
  }
}
