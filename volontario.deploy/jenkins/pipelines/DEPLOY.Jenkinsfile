pipeline {
    agent {
        label 'jdk-17'
    }

    stages {
        stage('Build images') {
            steps {
                echo 'Building backend image...'
                dir('volontario.server') {
                    sh 'docker build -t jenkins.volontario.me:5000/volontario-back:latest .'
                }
                echo 'Building frontend image...'
                dir('volontario.client/volontario.client.application/volontario') {
                    sh 'docker build -t jenkins.volontario.me:5000/volontario-front:latest --build-arg CLIENT_ENVIRONMENT=production .'
                }
                echo 'Clean old images...'
                sh 'docker rmi $(docker images | grep none | tr -s ' ' | cut -d ' ' -f3)'
                echo 'Pushing images...'
                sh 'docker push jenkins.volontario.me:5000/volontario-back:latest && docker push jenkins.volontario.me:5000/volontario-front:latest'
            }
        }
        stage('Deploy on dev') {
            steps {
                echo 'Deploying...'
                sh "ssh -o StrictHostKeyChecking=no volontario@dev.volontario.me 'docker compose down -v && docker volume rm $(docker volume ls -q) && docker compose pull && docker compose up -d'"
            }
        }
    }
}