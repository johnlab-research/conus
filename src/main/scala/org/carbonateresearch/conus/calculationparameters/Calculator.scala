package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, Previous}
import org.carbonateresearch.conus.common.{ModelCalculationSpace, ModelResults}

import scala.compat.Platform.EOL

// Tagging trait for model parameters case classes
trait Calculator {

  def calculate (step:Int,previousResults:ModelResults): ModelResults = ???
  def unit: String = ""
  def outputs:List[CalculationParametersIOLabels]
  def inputs:Option[List[CalculationParametersIOLabels]] = None
  def functionBlock: Option[List[Double] => Double] = None

  def checkForError(previousParameters:List[Calculator]): String = {
    val parameterList = previousParameters.flatMap(c => c.outputs)

    val checkPresenceOfParameter:String = inputs match{
      case None => ""
      case v:Some[List[CalculationParametersIOLabels]] => {v.get.map(i => {
        val j = i match {
          case j:Previous => j.input
          case _ => i
        }
        if(parameterList.contains(j)){
          "" }
        else {"Parameter '"+j.toString + "' missing or out of sequence when calculating '"+outputs.map(o => o.toString).mkString(", ")+"'"+EOL}}).mkString("")}}

    checkPresenceOfParameter

  }
}
