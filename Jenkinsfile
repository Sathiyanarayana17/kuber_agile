
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    environment {
        DOCKER_IMAGE = 'sathiya32/java-app:latest'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Verify JAR') {
            steps {
                bat '''
                    if not exist target\\simple-java-app-1.0.jar exit /b 1
                    jar tf target\\simple-java-app-1.0.jar | findstr /x "App.class"
                    if errorlevel 1 exit /b 1
                '''
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
                        credentialsId: 'dockerhub-pat-v2',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                        @echo off

                        powershell.exe -NoProfile -NonInteractive -ExecutionPolicy Bypass -Command "$env:DOCKER_PASS | docker login --username $env:DOCKER_USER --password-stdin; if ($LASTEXITCODE -ne 0) { exit 1 }"

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
                bat 'kubectl rollout restart deployment/java-app'
                bat 'kubectl rollout status deployment/java-app --timeout=180s'
            }
        }

        stage('Verify Deployment') {
            steps {
                bat 'kubectl get deployments'
                bat 'kubectl get pods'
                bat 'kubectl get services'
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Java build, Docker push, and Kubernetes deployment completed.'
        }

        failure {
            echo 'FAILED: Check the first failed stage in Console Output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
