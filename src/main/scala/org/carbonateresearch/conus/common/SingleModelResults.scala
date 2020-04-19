package org.carbonateresearch.conus.common
import java.lang.System.lineSeparator
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps

case class SingleModelResults(private val dataContainer: Map[Int,StepResults]) extends SimulationResults {
val EOL = lineSeparator()
  def size:Int = dataContainer.size
  def prettyPrint[T](k:ModelVariable[T],step:Int):String = {
    dataContainer(step).prettyPrint(k)
  }

  def printStepResults(step:Int):String = {
    "Step #"+step+""+dataContainer(step).allStepResultsString+EOL
  }

  def completeModelResultsString: String = {
    val mySteps:List[Int] = (0 until resultsForStep(0).get(NumberOfSteps).getOrElse(0)).toList
    mySteps.map(k => printStepResults(k)).foldLeft("")(_ + _)
  }

  def isDefinedAt(step:Int):Boolean = {
    dataContainer.isDefinedAt(step)
  }

  def variableIsDefinedAt[T](k:ModelVariable[T],step:Int):Boolean = {
    dataContainer(step).isDefinedAt(k)
  }

  def resultsForStep(stepNumber: Int): StepResults = dataContainer(stepNumber)

  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): Option[T] = {
    dataContainer(stepNumber).get(k)
  }

  def getStepResult(stepNumber:Int, k:CalculationParametersIOLabels): Any = {
    dataContainer(stepNumber).get(k)
  }

  def  getModelVariablesForStep(step:Int):List[CalculationParametersIOLabels] = dataContainer(step).getAllKeys

  def mergeWith(otherModelResults:SingleModelResults): SingleModelResults = SingleModelResults(this.dataContainer ++ otherModelResults.dataContainer)

  def addParameterResultAtLastStep[T](k:ModelVariable[T],v:T): SingleModelResults = {
    addDataAtStepLevel(lastStepNumber,k,v)
  }

  def addParameterResultAtNewStep[T](k:ModelVariable[T],v:T): SingleModelResults ={
    val newStepNumber:Int = dataContainer.size
    val newStep = StepResultsWithData(k, v)
    SingleModelResults(dataContainer++Map(newStepNumber->newStep))
  }

  def addParameterResultAtStep[T](k:ModelVariable[T],v:T,atStepNumber:Int): SingleModelResults = {
    if(atStepNumber<dataContainer.size){
    addDataAtStepLevel(atStepNumber,k,v)} else {
      addParameterResultAtNewStep(k,v)
    }
  }

  def addParameterResultsAtStep[T](m:Map[CalculationParametersIOLabels,Any],atStepNumber:Int): SingleModelResults = {
    SingleModelResults(dataContainer ++ Map(lastStepNumber->dataContainer(atStepNumber).add(m)))
  }

  def addStepResultAtStep[T](stepResult:StepResults,atStepNumber:Int): SingleModelResults = {
    SingleModelResults(dataContainer++Map(atStepNumber->stepResult))
  }

  private def addDataAtStepLevel[T](stepNumber:Int, k:ModelVariable[T],v:T):SingleModelResults = {
    val newStepResults:StepResults = lastStep.add(k,v)
    SingleModelResults(dataContainer ++ Map(stepNumber -> newStepResults))
  }

  private def lastStepNumber:Int = dataContainer.size-1
  private def lastStep:StepResults = dataContainer(lastStepNumber)

  def resultsPerLabel: ResultsPerModelVariable = {
      val keys:List[CalculationParametersIOLabels] = dataContainer(lastStepNumber).getAllKeys
    val steps:List[Int] = dataContainer.keys.toList

    ResultsPerModelVariable(keys.zip(keys.map(k => steps.zip(dataContainer.map(s => s._2.get(k)).toList).toMap)).toMap)
  }
}
