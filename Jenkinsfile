
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'sathiya32/java-app:latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Sathiyanarayana17/kuber_agile.git'
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE% .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                        @echo off
                        echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                        if errorlevel 1 exit /b 1
                        docker push %DOCKER_IMAGE%
                        if errorlevel 1 exit /b 1
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl apply -f deployment.yaml'
                bat 'kubectl rollout status deployment/java-app --timeout=120s'
            }
        }
    }

    post {
        success {
            echo 'CI/CD pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
