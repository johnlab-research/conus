package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels}
import spire.math.{Number, abs}
import spire.implicits._

final case class MultiplierFromStepsCP(outputValueLabel: CalculationParametersIOLabels, maxValue: Number, minValue: Number, increment: Number) extends CalculationParameters {

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

        val resultsList = maxValue-(step*increment)
        Map(step -> Map(outputValueLabel -> resultsList))
  }

}
