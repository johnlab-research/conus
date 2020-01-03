package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.common.ModelResults


final case class GeothermalGradientThroughTime(geothermalGradientsAgeMap:List[(Double, Double)]) extends CalculationStepValue with StandardsParameters{

  override val outputs  = List(GeothermalGradient)
  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

    InterpolateValues(inputValueLabel = Age, output = GeothermalGradient, xyList = geothermalGradientsAgeMap).calculate(step, previousResults)

  }

}
