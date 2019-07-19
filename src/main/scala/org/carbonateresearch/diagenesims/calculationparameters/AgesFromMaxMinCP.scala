package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class AgesFromMaxMinCP(maxAge: Number, minAge:Number) extends CalculationParameters{

   override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {

    MultiplierFromStepsCP(outputValueLabel ="Age", maxValue = maxAge, minValue = minAge).calculate(steps, previousResults)

  }

}
