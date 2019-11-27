package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO._
import spire.implicits._
import spire.math.Number


final case class Initializer(inputsList: List[(CalculationParametersIOLabels,Number)]) extends CalculationParameters {

  override def outputs:List[CalculationParametersIOLabels] = inputsList.map(i => i._1)

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    Map(step -> inputsList.toMap)
  }

}




