package org.carbonateresearch.conus.common

trait SimulationResults {
def resultsForStep(stepNumber: Int): StepResults
def getStepResult[T](stepNumber:Int, label:ModelVariable[T]): T
def mergeWith(otherModelResults:NumericalResults): NumericalResults
  def addParameterResultAtLastStep[T](label:ModelVariable[T],value:T): NumericalResults
  def addParameterResultAtNewStep[T](label:ModelVariable[T],value:T,newStepNumber:Int): NumericalResults
  def addParameterResultAtStep[T](label:ModelVariable[T],value:T,atStepNumber:Int): NumericalResults
}


