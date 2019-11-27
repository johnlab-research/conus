package org.carbonateresearch.conus.common

import akka.actor.Props
import org.carbonateresearch.conus.DiageneSim.actorSystem
import scala.compat.Platform.EOL
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.calculationparameters.{CalculateStepValue, CalculationParameters}
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

  /*
  def next(inputs: List[CalculationParametersIOLabels], outputs:List[Parameter], function: List[Number] => List[Number]): ModelCalculationSpace ={
      ModelCalculationSpace(calculations.map(cl => cl next CalculateStepValue(Some(inputs),outputs(0),function)),outputs.map(v => (v,Number(0))).toMap++this.parameters)

  }*/

  def ==(calibration:(CalculationParametersIOLabels,List[Number])*) : Unit = {}


  private def checkCalibratedModels: Unit = {}

  def size : Int = calculations.size


  def run: Unit = {

    val firstModels:List[CalculationParameters] = calculations(0).modelParameters
    //val parameterList = firstModels.flatMap(c => c.outputs).map(label => label.toString).reverse
    val parameterList = firstModels.map(c => c.outputs)
    //val errorsList:String = firstModels.map(c => c.checkForError).flatten.mkString(EOL)

    @tailrec
    def checkErrors (parametersList:List[CalculationParameters], currentString: String): String = {
      parametersList match  {
        case Nil => currentString
        case x::xs => checkErrors(xs,currentString+x.checkForError(xs))
      }
    }

    val errorsList :String = checkErrors(firstModels,"")


    if(errorsList == "") {
      println("No error detected, computing the following parameters in the model:" + EOL+parameterList.reverse.flatten.mkString(", ")+EOL+"----------------------------------------")
      val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])
      modeller ! calculations

    }
    else {
      println(errorsList+EOL+"Impossible to initiate a run: Correct error(s) first")}
  }



}


