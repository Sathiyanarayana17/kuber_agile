
stage('Push to Docker Hub') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-creds',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )
        ]) {
            powershell '''
                $env:DOCKER_PASS |
                    docker login --username $env:DOCKER_USER --password-stdin

                if ($LASTEXITCODE -ne 0) {
                    exit $LASTEXITCODE
                }

                docker push $env:DOCKER_IMAGE

                if ($LASTEXITCODE -ne 0) {
                    exit $LASTEXITCODE
                }

                docker logout
            '''
        }
    }
}
