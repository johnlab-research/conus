package org.carbonateresearch.diagenesims.common

import org.carbonateresearch.diagenesims.calculationparameters.CalculationParameters
import spire.math._
import spire.algebra._
import spire.implicits._

final case class Stepper (nbSteps:Int) {

  def prepareSteps:  List[Number] = (0 to nbSteps).toList.map(x => Number(x))

  def |->(parameter: CalculationParameters): ChainableCalculation = ChainableCalculation(prepareSteps, List(parameter))

}
