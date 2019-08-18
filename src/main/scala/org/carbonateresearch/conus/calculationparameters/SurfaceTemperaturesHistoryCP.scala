package org.carbonateresearch.conus.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._
import org.carbonateresearch.conus.calculationparameters.parametersIO.{Age, SurfaceTemperature, CalculationParametersIOLabels}

final case class SurfaceTemperaturesHistoryCP (SurfaceTemperatureAgeMap:List[(Number, Number)]) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolatorCP(inputValueLabel = Age, outputValueLabel = SurfaceTemperature, xyList = SurfaceTemperatureAgeMap).calculate(step,previousResults)

  }

}
