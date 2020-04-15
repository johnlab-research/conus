package org.carbonateresearch.conus.common

trait SimulationResults {
  def resultsForStep(stepNumber: Int): StepResults
  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): Option[T]
  def mergeWith(otherModelResults:SingleModelResults): SingleModelResults
  def addParameterResultAtLastStep[T](k:ModelVariable[T],v:T): SingleModelResults
  def addParameterResultAtNewStep[T](k:ModelVariable[T],v:T): SingleModelResults
  def addParameterResultAtStep[T](k:ModelVariable[T],v:T,atStepNumber:Int): SingleModelResults
  def prettyPrint[T](k:ModelVariable[T],step:Int):String
  def isDefinedAt(step:Int):Boolean
  def variableIsDefinedAt[T](k:ModelVariable[T],step:Int):Boolean
  def resultsPerLabel: ResultsPerModelVariable
}


