package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.CalculationParameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps}
import spire.math._
import spire.algebra._
import spire.implicits._

final case class Stepper (nbSteps:Int)  {

  def prepareSteps:  List[Number] = (0 to nbSteps).toList.map(x => Number(x))

  def |->(parameter: CalculationParameters): ChainableCalculation = ChainableCalculation(prepareSteps, List(parameter))

}
