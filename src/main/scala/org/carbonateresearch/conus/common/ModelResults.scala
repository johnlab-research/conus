package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps}


case class ModelResults(theseResults: Map[Int, SingleStepResults]){

  val steps = theseResults.keys

  def resultsForStep(step: Int): SingleStepResults = {
    theseResults(step)
  }

  def getStepResult(myStep:Int, label:CalculationParametersIOLabels):Double ={
    theseResults(myStep).valuesForAllLabels(label)
  }

  def mergeWith(otherModelResults:ModelResults):ModelResults = {
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

  ModelResults(mergedResultMap)}

  def addParameterResultAtLastStep(label:CalculationParametersIOLabels,value:Double): ModelResults = {
      ModelResults(theseResults++Map(theseResults.size-1 -> SingleStepResults(Map(label -> value))))
    }

  def addParameterResultAtNewStep(label:CalculationParametersIOLabels,value:Double,newStep:Int): ModelResults = {
      ModelResults(theseResults++Map(newStep -> SingleStepResults(Map(label -> value))))
    }

  def addParameterResultAtStep(label:CalculationParametersIOLabels,value:Double,atStep:Int): ModelResults = {
    val oldSingleStepResults: SingleStepResults = theseResults(atStep)
    val newSingleStepResults: SingleStepResults = SingleStepResults(oldSingleStepResults.valuesForAllLabels++Map(label -> value))
    ModelResults(theseResults++Map(atStep -> newSingleStepResults))
  }

  def addResult(value:Double): ResultFactory = {
    new ResultFactory(value=value)
  }

  private def addNewStepResult(newResult:Map[Int,SingleStepResults]): ModelResults = ModelResults(theseResults++newResult)

  private [ModelResults] class ResultFactory(step:Int = 0, label:CalculationParametersIOLabels = NumberOfSteps, value:Double, owner:ModelResults=this){
    def withLabel(newLabel:CalculationParametersIOLabels): ResultFactory = new ResultFactory(label=newLabel,value=value)
    def atStep(newStep:Int):ModelResults = {
      owner.addNewStepResult(Map(newStep -> SingleStepResults(Map(label -> value))))}
  }
}

object ModelResults {
  def apply():ModelResultsFactory = {new ModelResultsFactory}

  class ModelResultsFactory(){
    def newModelResultWithLabel(label:CalculationParametersIOLabels): ModelResultsFactoryWithLabel = new ModelResultsFactoryWithLabel(label)}

  class ModelResultsFactoryWithLabel(label:CalculationParametersIOLabels){
    def forValue(value:Double): ModelResultsFactoryWithValueAndLabel = new ModelResultsFactoryWithValueAndLabel(value,label)}

   class ModelResultsFactoryWithValueAndLabel(value:Double,label:CalculationParametersIOLabels){
    def atStep(step:Int): ModelResults = new ModelResults(Map(step->SingleStepResults(Map(label->value))))}

}
