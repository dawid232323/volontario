pipeline {
    agent {
        label 'jdk-17'
    }
    stages {
        stage('Build') {
            steps {
                dir('volontario.server') {
                    sh 'mvn clean install -Dmaven.test.skip'
                }
            }
        }
    }
}