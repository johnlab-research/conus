package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.{CalculationParameters, CalculationResults, ParalleledCPs}
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import spire.implicits._
import spire.math._
import spire.algebra._
import scala.annotation.tailrec

final case class ChainableCalculation(ID:Int, steps:List[Number], modelParameters:List[CalculationParameters]) {

  def +(nextModelParameters: CalculationParameters*): ChainableCalculation = {

    nextModelParameters.size match {
      case 0 => this
      case _ => {
        ChainableCalculation(ID,steps,  nextModelParameters.toList:::modelParameters)

      }
    }
  }

  def +(nextChainableCalculation: ChainableCalculation): ChainableCalculation = {

    ChainableCalculation(ID, steps,this.modelParameters ++ nextChainableCalculation.modelParameters)
  }

  def ==(secondChainableCalculation: ChainableCalculation): ChainableCalculation = {

     ChainableCalculation(ID, steps,  secondChainableCalculation.modelParameters:::modelParameters)
  }



    def evaluate: CalculationResults = {

    val inverseParams = modelParameters.reverse

    @tailrec
    def traverseSteps (stepsCounter:List[Number], currentResults: Map[Number, Map[CalculationParametersIOLabels,Number]]): Map[Number, Map[CalculationParametersIOLabels,Number]] = {
      stepsCounter match  {
        case Nil => currentResults
        case x::xs => traverseSteps(xs,currentResults++evaluateSingleStep(x,inverseParams, currentResults))
      }
    }
      val initialMap:Map[Number, Map[CalculationParametersIOLabels,Number]] = Map(Number(0) -> Map(NumberOfSteps -> (Number(steps.size))))
    CalculationResults(ID,steps, modelParameters, traverseSteps(steps,  initialMap))
    }


  def evaluateSingleStep(step:Number, parameters: List[CalculationParameters], currentResults: Map[Number, Map[CalculationParametersIOLabels,Number]]): Map[Number, Map[CalculationParametersIOLabels,Number]] = {

    @tailrec
    def evaluateSingleStepWithCounter (params: List[CalculationParameters],thisCurrentResults: Map[Number, Map[CalculationParametersIOLabels,Number]]): Map[Number, Map[CalculationParametersIOLabels,Number]] = {
      params match  {
        case Nil => thisCurrentResults
        case x::xs => evaluateSingleStepWithCounter(xs,thisCurrentResults ++ Map(step -> (thisCurrentResults(step) ++ (x.calculate(step,thisCurrentResults))(step))))
      }
    }

    val newStepResult:Map[Number, Map[CalculationParametersIOLabels,Number]] = Map(step -> Map(NumberOfSteps -> (Number(steps.size))))

     evaluateSingleStepWithCounter(parameters, currentResults ++ newStepResult)

  }

}


