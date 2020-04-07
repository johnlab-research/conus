package org.carbonateresearch.conus.oldies

import org.carbonateresearch.conus.common.SingleStepResults
import org.carbonateresearch.conus.equations.Calculator
import org.carbonateresearch.conus.equations.parametersIO.NumberOfSteps
import org.carbonateresearch.conus.util.TimeUtils

import scala.annotation.tailrec

final case class OldChainableCalculation(ID:Int, steps:List[Int], modelParameters:List[Calculator]) {

  def next(nextModelParameters: Calculator*): OldChainableCalculation = {

    nextModelParameters.size match {
      case 0 => this
      case _ => {
        OldChainableCalculation(ID,steps,  nextModelParameters.toList:::modelParameters)
      }
    }
  }

  def next(nextChainableCalculation: OldChainableCalculation): OldChainableCalculation = {

    OldChainableCalculation(ID, steps,this.modelParameters ++ nextChainableCalculation.modelParameters)
  }


  def ==(secondChainableCalculation: OldChainableCalculation): OldChainableCalculation = {

     OldChainableCalculation(ID, steps,  secondChainableCalculation.modelParameters:::modelParameters)
  }

  def evaluate(startTime:Double): OldSingleModelWithResults = {
    val inverseParams = modelParameters.reverse

    @tailrec
    def traverseSteps (stepsCounter:List[Int], currentResults: OldModelResults): OldModelResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))
      }
    }
      val initialModelResults:OldModelResults = OldModelResults(Map(0 -> SingleStepResults(Map(NumberOfSteps -> steps.size))))
    val evaluatedModel = OldSingleModelWithResults(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime)
    evaluatedModel
    }


  def evaluateSingleStep(step:Int, parameters: List[Calculator], currentResults: OldModelResults): OldModelResults = {

    @tailrec
    def evaluateSingleStepWithCounter (params: List[Calculator], thisCurrentResults: OldModelResults): OldModelResults = {
      params match  {
        case Nil => thisCurrentResults
        case x::xs => evaluateSingleStepWithCounter(xs,x.calculate(step,thisCurrentResults))
      }
    }
    val newModelResults = currentResults.addParameterResultAtNewStep(NumberOfSteps,steps.size,step)
    evaluateSingleStepWithCounter(parameters, newModelResults)
  }

  private def printOutputString(time:Double): Unit = {

    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar

    print(deleteSequence+"Model #"+this.ID+" completed in "+timeTaken)
  }

}
