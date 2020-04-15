package org.carbonateresearch.conus.common


final case class Initializer(inputsList: List[(CalculationParametersIOLabels,Any)]) extends Calculator {

  override def outputs=inputsList(0)._1

  override def calculate (step:Step): Step  = {
    val currentStepNumber:Int = step.stepNumber
    val previousResults:SingleModelResults = step.currentResults
    val stepError = step.stepErrors
    val newResults:SingleModelResults = if(currentStepNumber==0){
      previousResults.addParameterResultsAtStep(inputsList.toMap,currentStepNumber)}else{
      previousResults}
    Step(currentStepNumber,step.totalSteps,newResults,stepError)
  }

}




