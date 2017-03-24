node {

   stage('Preparation') {
     git 'https://github.com/evandor/skysail-ext.git'
   }

   stage('build') {
     sh './gradlew clean build'
   }

   stage('cucumber') {
  	 step([$class: 'CucumberReportPublisher', failedFeaturesNumber: 0, failedScenariosNumber: 0, failedStepsNumber: 0, fileExcludePattern: '', fileIncludePattern: '**/cucumber.json', jsonReportDirectory: '', parallelTesting: false, pendingStepsNumber: 0, skippedStepsNumber: 0, trendsLimit: 0, undefinedStepsNumber: 0])
   }

}
