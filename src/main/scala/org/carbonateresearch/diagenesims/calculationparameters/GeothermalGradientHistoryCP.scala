package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class GeothermalGradientHistoryCP(GeothermalGradientsAgeMap:List[(Number, Number)]) extends CalculationParameters {

  override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {

    InterpolatorCP(inputValueLabel = "Age", outputValueLabel ="Geothermal Gradient", xyList = GeothermalGradientsAgeMap).calculate(steps, previousResults)

  }

}
