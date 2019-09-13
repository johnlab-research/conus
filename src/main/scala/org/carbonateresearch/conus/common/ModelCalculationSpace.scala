package org.carbonateresearch.conus.common

import akka.actor.Props
import org.carbonateresearch.conus.DiageneSim.actorSystem
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.calculationparameters.{CalculationParameters, ParalleledCPs}
import spire.math._

import scala.annotation.tailrec

final case class ModelCalculationSpace(calculations:List[ChainableCalculation]) {


  def +(nextCalculationParameter: CalculationParameters): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.map(cl => cl + nextCalculationParameter))
  }

  def +(nextChainableCalculation: ChainableCalculation): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.map(cl => cl + nextChainableCalculation))
  }

  def +(nextModelCalculationSpace: ModelCalculationSpace): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.flatMap(cl => nextModelCalculationSpace.calculations.map(
      ncl => cl + ncl)))
  }

  def ==(calibration:(CalculationParametersIOLabels,List[Number])*) : Unit = {}


  private def checkCalibratedModels: Unit = {}

  def size : Int = calculations.size


  def run: Unit = {
    val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])
    modeller ! calculations
  }



}


