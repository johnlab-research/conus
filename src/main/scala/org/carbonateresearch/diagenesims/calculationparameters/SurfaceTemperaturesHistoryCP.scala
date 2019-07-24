package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class SurfaceTemperaturesHistoryCP (SurfaceTemperatureAgeMap:List[(Number, Number)]) extends CalculationParameters {

  override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {

    InterpolatorCP(inputValueLabel = "Age", outputValueLabel ="Surface Temperature", xyList = SurfaceTemperatureAgeMap).calculate(steps,previousResults)

  }

}
