package org.carbonateresearch.conus.calculationparameters

import spire.math.Number
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import spire.implicits._

final case class GeothermalGradientHistoryCP(geothermalGradientsAgeMap:List[(Number, Number)]) extends CalculationParameters with StandardsParameters{

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolatorCP(inputValueLabel = Age, outputValueLabel = GeothermalGradient, xyList = geothermalGradientsAgeMap).calculate(step, previousResults)

  }

}
