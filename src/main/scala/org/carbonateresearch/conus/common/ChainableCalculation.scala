package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.CalculationStepValue
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import scala.annotation.tailrec
import org.carbonateresearch.conus.calculationparameters.CalculateStepValue
import org.carbonateresearch.conus.common

final case class ChainableCalculation(ID:Int, steps:List[Int], modelParameters:List[CalculationStepValue]) {

  def next(nextModelParameters: CalculationStepValue*): ChainableCalculation = {

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

  def evaluate: SingleModelWithResults = {

    val inverseParams = modelParameters.reverse

    @tailrec
    def traverseSteps (stepsCounter:List[Int], currentResults: ModelResults): ModelResults = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => traverseSteps(xs,evaluateSingleStep(x,inverseParams, currentResults))
      }
    }
      val initialModelResults:ModelResults = ModelResults(Map(0 -> SingleStepResults(Map(NumberOfSteps -> steps.size))))
    SingleModelWithResults(ID,steps, modelParameters, traverseSteps(steps,  initialModelResults))
    }


  def evaluateSingleStep(step:Int, parameters: List[CalculationStepValue], currentResults: ModelResults): ModelResults = {

    @tailrec
    def evaluateSingleStepWithCounter (params: List[CalculationStepValue], thisCurrentResults: ModelResults): ModelResults = {
      params match  {
        case Nil => thisCurrentResults
        case x::xs => evaluateSingleStepWithCounter(xs,x.calculate(step,thisCurrentResults))
      }
    }
    val newModelResults = currentResults.addParameterResultAtNewStep(NumberOfSteps,steps.size,step)
    evaluateSingleStepWithCounter(parameters, newModelResults)
  }

}


