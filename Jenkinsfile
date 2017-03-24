node {
   stage('Preparation') {
     git 'https://github.com/evandor/skysail-ext.git'
   }
   stage('build') {
     sh './gradlew clean build'
   }
}
