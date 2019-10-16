package org.carbonateresearch.conus.common

import akka.actor.Props
import org.carbonateresearch.conus.DiageneSim.actorSystem
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.calculationparameters.{CalculateValueForStep, CalculationParameters, ParalleledCPs}
import spire.math._

import scala.annotation.tailrec

final case class ModelCalculationSpace(calculations:List[ChainableCalculation], parameters: Map[Parameter,Number]) {


  def next(nextCalculationParameter: CalculationParameters): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.map(cl => cl next nextCalculationParameter), Map())
  }

  def next(nextChainableCalculation: ChainableCalculation): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.map(cl => cl next nextChainableCalculation), Map())
  }

  def next(nextModelCalculationSpace: ModelCalculationSpace): ModelCalculationSpace = {

    ModelCalculationSpace(calculations.flatMap(cl => nextModelCalculationSpace.calculations.map(
      ncl => cl next ncl)), Map())
  }

  def next(inputs: List[CalculationParametersIOLabels], outputs:List[Parameter], function: List[Number] => List[Number]): ModelCalculationSpace ={
      ModelCalculationSpace(calculations.map(cl => cl next CalculateValueForStep(inputs,outputs,function)),outputs.map(v => (v,Number(0))).toMap++this.parameters)

  }

  def ==(calibration:(CalculationParametersIOLabels,List[Number])*) : Unit = {}


  private def checkCalibratedModels: Unit = {}

  def size : Int = calculations.size


  def run: Unit = {
    val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])
    modeller ! calculations
  }



}


