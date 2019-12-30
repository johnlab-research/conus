package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, Previous}
import spire.math._
import scala.compat.Platform.EOL
import spire.algebra._
import spire.implicits._

import scala.annotation.tailrec

// Tagging trait for model parameters case classes
trait CalculationStepValue {
  def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]] = ???
  def unit: String = ""
  def outputs:List[CalculationParametersIOLabels]
  def inputs:Option[List[CalculationParametersIOLabels]] = None
  def functionBlock: Option[List[Number] => Number] = None

  def checkForError(previousParameters:List[CalculationStepValue]): String = {

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
