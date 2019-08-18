package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{Age, Depth, CalculationParametersIOLabels}
import spire.math.{Number, Numeric}
import spire.implicits._

final case class AgesToDepthCP(ageModel:List[(Number, Number)]) extends CalculationParameters {

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolatorCP(inputValueLabel = Age, outputValueLabel =Depth, xyList = ageModel).calculate(step, previousResults)

  }

}
