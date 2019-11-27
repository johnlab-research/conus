package org.carbonateresearch.conus.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}

final case class SurfaceTemperaturesThroughTime(SurfaceTemperatureAgeMap:List[(Number, Number)]) extends CalculationParameters with StandardsParameters {

  override val outputs: List[CalculationParametersIOLabels] = List(SurfaceTemperature)
  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolateValues(inputValueLabel = Age, output = SurfaceTemperature, xyList = SurfaceTemperatureAgeMap).calculate(step,previousResults)

  }

}
