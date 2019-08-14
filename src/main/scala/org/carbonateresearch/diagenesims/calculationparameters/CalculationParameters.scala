package org.carbonateresearch.diagenesims.calculationparameters
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO.CalculationParametersIOLabels
import spire.math._
import spire.algebra._
import spire.implicits._

import scala.annotation.tailrec

// Tagging trait for model parameters case classes
trait CalculationParameters {
  def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]] = ???
  def unit: String = ""
}
