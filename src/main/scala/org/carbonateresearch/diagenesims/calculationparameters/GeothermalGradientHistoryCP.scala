package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number}
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO.{GeothermalGradient, Depth, Age, CalculationParametersIOLabels}
import spire.implicits._

final case class GeothermalGradientHistoryCP(geothermalGradientsAgeMap:List[(Number, Number)]) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolatorCP(inputValueLabel = Age, outputValueLabel = GeothermalGradient, xyList = geothermalGradientsAgeMap).calculate(step, previousResults)

  }

}
