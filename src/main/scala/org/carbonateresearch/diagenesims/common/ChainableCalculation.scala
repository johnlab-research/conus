package org.carbonateresearch.diagenesims.common

import org.carbonateresearch.diagenesims.calculationparameters.{CalculationParameters, CalculationResults, ParalleledCPs}
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
    def evaluateWithCounter (params: List[CalculationParameters],currentResults: Map[String, Map[Number,Number]]): Map[String, Map[Number,Number]] = {
      params match  {
        case Nil => currentResults
        case x::xs => evaluateWithCounter(xs,currentResults++x.calculate(steps,currentResults))
      }
    }

    CalculationResults(steps, modelParameters, evaluateWithCounter(inverseParams,Map()))

    //Map[String, Map[Number,Number]]
    }


}


