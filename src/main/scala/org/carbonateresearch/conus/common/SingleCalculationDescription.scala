package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.common.Step
import shapeless.HMap

case class SingleCalculationDescription[T](f:Step=>T, label:ModelVariable[T]) extends Calculator {
  def output: ModelVariable[T] = label
  override def calculate(step: Step): Step = {
    val currentStepNumber:Int = step.stepNumber
    val oldResults:NumericalResults = step.currentResults
    val stepError = step.stepErrors
    val calculatedValue:T = f(step)
    val newResults:NumericalResults = oldResults.addParameterResultAtStep(label,calculatedValue,currentStepNumber)
    Step(currentStepNumber,newResults,stepError)
  }
}
