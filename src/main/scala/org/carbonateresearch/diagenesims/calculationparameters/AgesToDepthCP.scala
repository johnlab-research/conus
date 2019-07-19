package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class AgesToDepthCP(ageModel:List[(Number, Number)]) extends CalculationParameters {

   override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {

    InterpolatorCP(inputValueLabel = "Age", outputValueLabel ="Depth", xyList = ageModel).calculate(steps, previousResults)

  }

}
