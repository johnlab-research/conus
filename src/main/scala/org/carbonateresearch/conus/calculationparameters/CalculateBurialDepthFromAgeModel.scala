package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, Depth, StandardsParameters}
import org.carbonateresearch.conus.common.ModelResults

final case class CalculateBurialDepthFromAgeModel(ageModel:List[(Double, Double)]) extends CalculationStepValue with StandardsParameters {

  override def outputs = List(Depth)
  override val inputs = Option(List(Age))

   override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

    InterpolateValues(inputValueLabel = Age, output =Depth, xyList = ageModel).calculate(step, previousResults)

  }

}
