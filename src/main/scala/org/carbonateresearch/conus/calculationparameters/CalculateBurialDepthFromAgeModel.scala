package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, Depth, StandardsParameters}
import spire.math.{Number, Numeric}
import spire.implicits._

final case class CalculateBurialDepthFromAgeModel(ageModel:List[(Number, Number)]) extends CalculationStepValue with StandardsParameters {

  override def outputs = List(Depth)
  override val inputs = Option(List(Age))

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    InterpolateValues(inputValueLabel = Age, output =Depth, xyList = ageModel).calculate(step, previousResults)

  }

}
