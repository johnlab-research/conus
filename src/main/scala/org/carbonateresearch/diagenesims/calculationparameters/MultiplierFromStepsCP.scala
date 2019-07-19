package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric, abs}
import spire.implicits._

final case class MultiplierFromStepsCP(outputValueLabel: String, maxValue: Number, minValue: Number) extends CalculationParameters {

   override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {

        val numberOfSteps = steps.size-1
        val increment = abs(maxValue-minValue)/(numberOfSteps)
        val resultsList = steps.map(stepNb => (stepNb, (maxValue-(stepNb*increment)))).toMap
        Map(outputValueLabel -> resultsList)

  }

}
