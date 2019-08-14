package org.carbonateresearch.diagenesims.common

import org.carbonateresearch.diagenesims.calculationparameters.{CalculationParameters, CalculationResults, ParalleledCPs}
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO._
import spire.implicits._
import spire.math._
import spire.algebra._
import scala.annotation.tailrec

final case class ChainableCalculation(steps:List[Number], modelParameters:List[CalculationParameters]) {

  /*
  def |->(nextModelParameters: CalculationParameters): ChainableCalculation = {
    ChainableCalculation(steps,  nextModelParameters::modelParameters)
  }*/

  def |->(nextModelParameters: CalculationParameters*): ChainableCalculation = {

    nextModelParameters.size match {
      case 0 => this
      case _ => {
        ChainableCalculation(steps,  nextModelParameters.toList:::modelParameters)

      }
    }
  }

  def ||->(nextModelParameters: CalculationParameters*): ChainableCalculation = {

    nextModelParameters.size match {
      case 0 => this
      case 1 => {
        ChainableCalculation(steps,  nextModelParameters(0)::modelParameters)

      }
      case _ => {
        val paralleledCP = ParalleledCPs(nextModelParameters.toVector)
        ChainableCalculation(steps,  paralleledCP::modelParameters)}
    }
  }

  def ->(secondChainableCalculation: ChainableCalculation): ChainableCalculation = {

     ChainableCalculation(steps,  secondChainableCalculation.modelParameters:::modelParameters)
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
    CalculationResults(steps, modelParameters, traverseSteps(steps,  initialMap))
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


