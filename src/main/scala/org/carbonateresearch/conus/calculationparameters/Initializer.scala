package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO._
import spire.implicits._
import spire.math.Number


final case class Initializer(inputs: List[(CalculationParametersIOLabels,Number)]) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    Map(step -> inputs.toMap)
  }

}




