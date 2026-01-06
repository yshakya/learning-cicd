pipeline {
    agent any
        // docker {
        //     image 'yamanshakya/ssh-client'
        //     args '-u 0:0'
        // }
    // }

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
        stage('SSH Key Access') {
            steps {
                sh """
                touch mykey.pem
                echo $SSH_KEY64 | base64 -d > mykey.pem
                chmod 400 mykey.pem
                ssh-keygen -R ${params.SERVER_IP}
                """
            }   
        }
        stage('Deploy Code to Server') {
            steps {
                sh """
                ssh -t ubuntu@${params.SERVER_IP} -i mykey.pem 'cd /var/www/html && git pull origin main'
                """
            }
        }

    }
}

