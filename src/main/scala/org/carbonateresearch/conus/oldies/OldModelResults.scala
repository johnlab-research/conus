package org.carbonateresearch.conus.oldies

import org.carbonateresearch.conus.common.SingleStepResults
import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, NumberOfSteps}


case class OldModelResults(theseResults: Map[Int, SingleStepResults]){

  private val steps = theseResults.keys

  def resultsForStep(step: Int): SingleStepResults = {
    theseResults(step)
  }

  def getStepResult(myStep:Int, label:CalculationParametersIOLabels):Double ={
    theseResults(myStep).valuesForAllLabels(label)
  }

  def mergeWith(otherModelResults:OldModelResults):OldModelResults = {
    val otherResults: Map[Int, SingleStepResults] = otherModelResults.theseResults
    val allKeys: Iterable[Int] = theseResults.keys++otherResults.keys
    type MapType = Map[Int, SingleStepResults]

    val mergedResultMap:Map[Int,SingleStepResults] = allKeys.map{k => {
        if(theseResults.isDefinedAt(k) && otherResults.isDefinedAt(k)){
          val theseSingleStepResults: SingleStepResults = theseResults(k)
          val otherSingleStepResults: SingleStepResults = otherResults(k)
          val newSingleStepResults:SingleStepResults = SingleStepResults(theseSingleStepResults.valuesForAllLabels++otherSingleStepResults.valuesForAllLabels)
          (k,newSingleStepResults)
        }
        else if(theseResults.isDefinedAt(k)){
          (k,theseResults(k))
        }
        else {
          (k,otherResults(k))
        }
      }
    }.toMap

  OldModelResults(mergedResultMap)}

  def addParameterResultAtLastStep(label:CalculationParametersIOLabels,value:Double): OldModelResults = {
      OldModelResults(theseResults++Map(theseResults.size-1 -> SingleStepResults(Map(label -> value))))
    }

  def addParameterResultAtNewStep(label:CalculationParametersIOLabels,value:Double,newStep:Int): OldModelResults = {
      OldModelResults(theseResults++Map(newStep -> SingleStepResults(Map(label -> value))))
    }

  def addParameterResultAtStep(label:CalculationParametersIOLabels,value:Double,atStep:Int): OldModelResults = {
    val oldSingleStepResults: SingleStepResults = theseResults(atStep)
    val newSingleStepResults: SingleStepResults = SingleStepResults(oldSingleStepResults.valuesForAllLabels++Map(label -> value))
    OldModelResults(theseResults++Map(atStep -> newSingleStepResults))
  }

  def addResult(value:Double): ResultFactory = {
    new ResultFactory(value=value)
  }

  private def addNewStepResult(newResult:Map[Int,SingleStepResults]): OldModelResults = OldModelResults(theseResults++newResult)

  private [OldModelResults] class ResultFactory(step:Int = 0, label:CalculationParametersIOLabels = NumberOfSteps, value:Double, owner:OldModelResults=this){
    def withLabel(newLabel:CalculationParametersIOLabels): ResultFactory = new ResultFactory(label=newLabel,value=value)
    def atStep(newStep:Int):OldModelResults = {
      owner.addNewStepResult(Map(newStep -> SingleStepResults(Map(label -> value))))}
  }
}

object OldModelResults {
  def apply():ModelResultsFactory = {new ModelResultsFactory}

  class ModelResultsFactory(){
    def newModelResultWithLabel(label:CalculationParametersIOLabels): ModelResultsFactoryWithLabel = new ModelResultsFactoryWithLabel(label)}

  class ModelResultsFactoryWithLabel(label:CalculationParametersIOLabels){
    def forValue(value:Double): ModelResultsFactoryWithValueAndLabel = new ModelResultsFactoryWithValueAndLabel(value,label)}

   class ModelResultsFactoryWithValueAndLabel(value:Double,label:CalculationParametersIOLabels){
    def atStep(step:Int): OldModelResults = new OldModelResults(Map(step->SingleStepResults(Map(label->value))))}

}
