pipeline {
    agent any

    environment {
        SSH_KEY64 = credentials('SSH_KEY64')
    }

    parameters {
            string(
                name: 'SERVER_IP',
                defaultValue: '100.31.25.234',
                description: 'Target Server'
        )
    }

    stages {
        stage('Configure SSH') {
            steps {
                sh '''
                mkdir -p ~/.ssh 
                chmod 700 ~/.ssh
                echo -e "Host *\n\tStrictHostKeyChecking no\n\n" > ~/.ssh/config
                cat ~/.ssh/config
                touch ~/.ssh/known_hosts
                chmod 600 ~/.ssh/known_hosts
                '''
            }

        stage('SSH Key Access') {
            steps {
                sh '''
                touch mykey.pem
                echo $SSH_KEY64 | base64 -d > mykey.pem
                chmod 400 mykey.pem
                ssh-keygen -R ${params.SERVER_IP}
                '''
            }    
        }
        }

    }
}

