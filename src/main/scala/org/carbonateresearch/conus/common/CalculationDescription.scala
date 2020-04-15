package org.carbonateresearch.conus.common

case class CalculationDescription[T](f:Step=>T, label:ModelVariable[T]) extends Calculator {
  override def outputs: CalculationParametersIOLabels = label.asInstanceOf[CalculationParametersIOLabels]

  override def calculate(step: Step): Step = {
    val currentStepNumber:Int = step.stepNumber
    val oldResults:SingleModelResults = step.currentResults
    val stepError = step.stepErrors
    val calculatedValue:T = f(step)
    val newResults:SingleModelResults = oldResults.addParameterResultAtStep(label,calculatedValue,currentStepNumber)
    Step(currentStepNumber,step.totalSteps,newResults,stepError)
  }
}
