pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean build'
      }
    }
  }
  environment {
    DOCKER_API_VERSION = '1.24'
  }
}