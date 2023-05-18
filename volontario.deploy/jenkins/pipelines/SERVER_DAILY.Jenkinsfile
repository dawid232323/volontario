pipeline {
    agent {
        docker {
            cloud 'docker' 
            label 'jdk-17'
        }
    }
    stages {
        stage('Build') {
            steps {
                dir('volontario.server') {
                    sh 'mvn clean install' // Run the Maven command inside the "volontario.server" directory
                }
            }
        }
    }
}