package org.carbonateresearch.conus.oldies

import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, Previous}

// Tagging trait for model parameters case classes
trait OldCalculator {

  def calculate (step:Int,previousResults:OldModelResults): OldModelResults = ???
  def unit: String = ""
  def outputs:List[CalculationParametersIOLabels]
  def inputs:Option[List[CalculationParametersIOLabels]] = None
  def functionBlock: Option[List[Double] => Double] = None

  def checkForError(previousParameters:List[OldCalculator]): String = {
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
        else {"Parameter '"+j.toString + "' missing or out of sequence when calculating '"+outputs.map(o => o.toString).mkString(", ")+"'"}}).mkString("")}}

    checkPresenceOfParameter

  }
}
