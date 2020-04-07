package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.common.Step
import shapeless.HMap

case class NumericalResults(currentResults: Map[Int,StepResults]) extends SimulationResults {
  def resultsForStep(stepNumber: Int): StepResults = currentResults(stepNumber)

  def getStepResult[T](stepNumber:Int, label:ModelVariable[T]): T = {
    implicit val implicitBiMapIS = new BiMapIS[ModelVariable[T],T]
    currentResults(stepNumber).dataContainer.get(label).getOrElse(label.defaultValue)
  }

  def mergeWith(otherModelResults:NumericalResults): NumericalResults = NumericalResults(this.currentResults ++ otherModelResults.currentResults)

  def addParameterResultAtLastStep[T](label:ModelVariable[T],value:T): NumericalResults = {
    implicit val implicitBiMapIS = new BiMapIS[ModelVariable[T],T]
    val lastStepNumber:Int = currentResults.size-1
    val newResultsValues:HMap[BiMapIS] = currentResults(lastStepNumber).dataContainer + (label -> value)
    val newStepResults = StepResults(newResultsValues)

    NumericalResults(currentResults ++ Map(lastStepNumber -> newStepResults))
  }

  def addParameterResultAtNewStep[T](label:ModelVariable[T],value:T,newStepNumber:Int): NumericalResults ={
    implicit val implicitBiMapIS = new BiMapIS[ModelVariable[T],T]

    val newResultsValues:HMap[BiMapIS] = HMap[BiMapIS](label -> value)
    val newStepResults = StepResults(newResultsValues)

    NumericalResults(currentResults ++ Map(newStepNumber -> newStepResults))
  }

  def addParameterResultAtStep[T](label:ModelVariable[T],value:T,atStepNumber:Int): NumericalResults = {
    implicit val implicitBiMapIS = new BiMapIS[ModelVariable[T],T]

    val newResultsValues:HMap[BiMapIS] = currentResults(atStepNumber).dataContainer + (label -> value)
    val newStepResults = StepResults(newResultsValues)

    NumericalResults(currentResults ++ Map(atStepNumber -> newStepResults))
  }
}
