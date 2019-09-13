package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, Depth, StandardsIOLabels}
import spire.math.{Number, Numeric}
import spire.implicits._

final case class BurialDepthCP(ageModel:List[(Number, Number)]) extends CalculationParameters with StandardsIOLabels {

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolatorCP(inputValueLabel = Age, outputValueLabel =Depth, xyList = ageModel).calculate(step, previousResults)

  }

}
