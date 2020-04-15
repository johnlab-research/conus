package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.common.Calculator
//import org.carbonateresearch.conus.equations.parametersIO._
import scala.annotation.tailrec
import org.carbonateresearch.conus.util.TimeUtils
import org.carbonateresearch.conus.util.Implicits._
import org.carbonateresearch.conus.util.CommonModelVariables._
import java.lang.System.lineSeparator


final case class ChainableCalculation(ID:Int, steps:List[Int], modelParameters:List[Calculator]) {
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

  def evaluate(startTime:Double): RunnedModel = {
    val inverseParams = modelParameters.reverse

    @tailrec
    def traverseSteps (stepsCounter:List[Int], currentResults: SingleModelResults): SingleModelResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => {
          //println(stepsCounter.head)
          traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))}
      }
    }

    val initialModelResults:SingleModelResults = SingleModelResults(Map())
    val evaluatedModel = RunnedModel(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime,evaluatedModel)

    evaluatedModel
    }


  def evaluateSingleStep(stepID:Int, parameters: List[Calculator], previousResults:SingleModelResults): SingleModelResults = {

    val newModelResults = previousResults.addParameterResultAtNewStep(NumberOfSteps,steps.size-1)
    val initialStep = Step(stepID,this.steps.size,newModelResults,"")

    @tailrec
    def evaluateSingleStepWithCounter (params: List[Calculator], currentStep: Step): Step = {

      params match  {
        case Nil => {
          currentStep}
        case x::xs => {
          //println("Now doing op "+params.size+" for operation "+params.head.toString)
          evaluateSingleStepWithCounter(xs,x.calculate(currentStep))}
      }
    }

    evaluateSingleStepWithCounter(parameters,initialStep).currentResults
  }

  private def printOutputString(time:Double,model:RunnedModel): Unit = {

    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar

    print("Model #"+model.ID+" completed in "+timeTaken+":"+EOL + model.summary)
  }

}


