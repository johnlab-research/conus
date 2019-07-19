package org.carbonateresearch.diagenesims.ageSteppedModels

import org.carbonateresearch.diagenesims.calculationparameters.{CalculationParameters, CalculationResults}
import spire.implicits._
import spire.math._
import spire.algebra._
import scala.annotation.tailrec

final case class ChainableCalculation(steps:List[Number], modelParameters:List[CalculationParameters]) {

  def +(nextModelParameters: CalculationParameters): ChainableCalculation = {
    ChainableCalculation(steps,  nextModelParameters::modelParameters)
  }

  def +(secondChainableCalculation: ChainableCalculation): ChainableCalculation = {

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


