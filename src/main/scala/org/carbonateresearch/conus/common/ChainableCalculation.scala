package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.Calculator
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import scala.annotation.tailrec
import org.carbonateresearch.conus.util.TimeUtils
import org.carbonateresearch.conus.util.StepFunctionUtils._

final case class ChainableCalculation(ID:Int, steps:List[Step], modelParameters:List[Calculator]) {

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
    def traverseSteps (stepsCounter:List[Int], currentResults: ModelResults): ModelResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))
      }
    }
      val initialModelResults:ModelResults = ModelResults(Map(0 -> SingleStepResults(Map(NumberOfSteps -> steps.size))))
    val evaluatedModel = SingleModelWithResults(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime)
    evaluatedModel
    }


  def evaluateSingleStep(step:Int, parameters: List[Calculator], currentResults: ModelResults): ModelResults = {

    @tailrec
    def evaluateSingleStepWithCounter (params: List[Calculator], thisCurrentResults: ModelResults): ModelResults = {
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


