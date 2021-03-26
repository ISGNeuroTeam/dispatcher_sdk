pipeline {
    agent any
    environment {
        NEXUS_COMMON_CREDS = credentials('nexus_cred')
    	NEXUS_COMMON_CREDS_USR = credentials('nexus_cred')
	NEXUS_COMMON_CREDS_PSW = credentials('nexus_cred')
    }
    stages {
        stage('Test') {
            steps{
                sh '''
                      make test ''' 
            }
        }
        stage ('Build'){
            steps{
                sh '''
		      make build
                      '''
            }

        }
        stage ('Delivery'){
            steps{
                script{
                 sh '''
			sbt publish
                      '''
                } 
            }
        }

  }
   post {
       always {
            step([$class: 'GitHubCommitStatusSetter'])
       }
       success {
            sh '''echo Find package in $NEXUS_REPO_URL/repository/components/ot.platform-sbt-releases'''
    }
}
}
