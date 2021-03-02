pipeline {
    agent { label 'jdk8-with-img' }

    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '20'))
        disableConcurrentBuilds()
    }

    environment {
        REVISION = getRevision("${GIT_BRANCH}", "${BUILD_NUMBER}")
    }

    stages {
        stage("Build") {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'SETTINGS_XML')]) {
                    sh "mvn -s ${SETTINGS_XML} -B -U -P prod -Drevision=${REVISION} -DbuildNumber=${BUILD_NUMBER} clean install"
                }
            }
        }
    }
}

def getRevision(String branch, String buildNumber) {
    pom = readMavenPom file: 'pom.xml'
    pomRevision = pom.properties['revision']

    if(branch == "master") {
        if(pomRevision.endsWith("-SNAPSHOT")) {
            pomRevision = pomRevision.take(pomRevision.lastIndexOf('-'))
        }
        return pomRevision + "-" + buildNumber
    }
    return pomRevision
}
