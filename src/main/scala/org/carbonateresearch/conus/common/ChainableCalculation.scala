package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.common.Calculator
//import org.carbonateresearch.conus.equations.parametersIO._
import scala.annotation.tailrec
import org.carbonateresearch.conus.util.TimeUtils
import org.carbonateresearch.conus.util.Implicits._
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps
import shapeless.HMap

final case class ChainableCalculation(ID:Int, steps:List[Int], modelParameters:List[Calculator]) {

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

  def evaluate(startTime:Double): SingleModelWithResults = {
    val inverseParams = modelParameters.reverse

    @tailrec
    def traverseSteps (stepsCounter:List[Int], currentResults: NumericalResults): NumericalResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))
      }
    }

    val dataContainer =  HMap[BiMapIS](NumberOfSteps -> steps.size)
    val initialModelResults:NumericalResults = NumericalResults(Map(0 -> StepResults(dataContainer)))
    val evaluatedModel = SingleModelWithResults(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime)
    evaluatedModel
    }


  def evaluateSingleStep(stepID:Int, parameters: List[Calculator], previousResults:NumericalResults): NumericalResults = {

    val newModelResults = previousResults.addParameterResultAtNewStep(NumberOfSteps,steps.size,stepID)
    val initialStep = Step(stepID,newModelResults,"")

    @tailrec
    def evaluateSingleStepWithCounter (params: List[Calculator], currentStep: Step): Step = {
      params match  {
        case Nil => currentStep
        case x::xs => evaluateSingleStepWithCounter(xs,x.calculate(currentStep))
      }
    }
    evaluateSingleStepWithCounter(parameters,initialStep).currentResults
  }

  private def printOutputString(time:Double): Unit = {

    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar

    print(deleteSequence+"Model #"+this.ID+" completed in "+timeTaken)
  }

}


