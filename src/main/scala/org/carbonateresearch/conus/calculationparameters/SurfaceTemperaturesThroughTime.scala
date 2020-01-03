package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.common.ModelResults

final case class SurfaceTemperaturesThroughTime(SurfaceTemperatureAgeMap:List[(Double, Double)]) extends CalculationStepValue with StandardsParameters {

  override val outputs: List[CalculationParametersIOLabels] = List(SurfaceTemperature)

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

    InterpolateValues(inputValueLabel = Age, output = SurfaceTemperature, xyList = SurfaceTemperatureAgeMap).calculate(step,previousResults)

  }

}
