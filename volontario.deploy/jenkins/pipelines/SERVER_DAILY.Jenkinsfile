pipeline {
    agent {
        label 'jdk-17'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}