pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Verify Environment') {
            steps {
                bat 'java --version'
                bat 'mvn --version'
            }
        }

        stage('Clean and Test') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'course-registration-app/target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build, tests and packaging completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the build logs and test reports.'
        }
    }
}
