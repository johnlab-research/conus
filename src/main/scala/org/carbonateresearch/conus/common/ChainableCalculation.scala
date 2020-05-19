package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.common.Calculator
//import org.carbonateresearch.conus.equations.parametersIO._
import scala.annotation.tailrec
import org.carbonateresearch.conus.util.TimeUtils
import org.carbonateresearch.conus.util.CommonModelVariables._
import java.lang.System.lineSeparator

abstract final case class ChainableCalculation(ID:Int, steps:List[Int], modelParameters:List[Calculator]) { /*
val EOL = lineSeparator()
  def next(nextModelParameters: Calculator*): ChainableCalculation = {
    nextModelParameters.size match {
      case 0 => this
      case _ => {
        ChainableCalculation(ID,steps,  nextModelParameters.toList:::modelParameters)
      }
    }
  }

  def next(nextChainableCalculation: ChainableCalculation): ChainableCalculation = {
    ChainableCalculation(ID, steps,this.modelParameters ++ nextChainableCalculation.modelParameters)
  }

  def ==(secondChainableCalculation: ChainableCalculation): ChainableCalculation = {
    ChainableCalculation(ID, steps,  secondChainableCalculation.modelParameters:::modelParameters)
  }

  def evaluate(startTime:Double): EvaluatedModel = {
    val inverseParams = modelParameters.reverse
    @tailrec
    def traverseSteps (stepsCounter:List[Int], currentResults: SingleModelResults): SingleModelResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => {
          traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))}
      }
    }
    val initialModelResults:SingleModelResults = SingleModelResults(Map())
    val evaluatedModel = EvaluatedModel(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime,evaluatedModel)
    evaluatedModel
    }

  def evaluateSingleStep(stepID:Int, parameters: List[Calculator], previousResults:SingleModelResults): SingleModelResults = {
    val newModelResults:SingleModelResults = previousResults.addParameterResultAtStep(NumberOfSteps,steps.size,stepID)
    val initialStep = Step(stepID,this.steps.size,newModelResults,"")
    @tailrec
    def evaluateSingleStepWithCounter (params: List[Calculator], currentStep: Step): Step = {
      params match  {
        case Nil => {
          currentStep}
        case x::xs => {
          evaluateSingleStepWithCounter(xs,x.calculate(currentStep))}
      }
    }
    evaluateSingleStepWithCounter(parameters,initialStep).grid
  }

  private def printOutputString(time:Double,model:EvaluatedModel): Unit = {
    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar
    print("Model #"+model.ID+" completed in "+timeTaken+":"+EOL + model.summary)
  }

  def formatHoursMinuteSeconds(nannoseconds:Double): String = {
    val totalTimeInSeconds:Double = nannoseconds/10E8
    val hours:Int = (totalTimeInSeconds/(60*60)).toInt
    val secondsRemainingForMinutes =  (totalTimeInSeconds % (60*60)).toInt
    val minutes:Int =(secondsRemainingForMinutes/60).toInt
    val seconds:Int =(secondsRemainingForMinutes % 60).toInt
    val  timeString:String = if(hours>0 && minutes>0){hours+" hours, " + minutes + " minutes, and " + seconds + " seconds"}
    else if(hours==0 && minutes>0){ minutes + " minutes, and " + seconds + " seconds"}
    else {seconds + " seconds"}
    timeString
  }
*/
}


