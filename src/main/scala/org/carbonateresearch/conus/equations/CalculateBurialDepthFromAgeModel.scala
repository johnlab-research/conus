package org.carbonateresearch.conus.equations
import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, Depth, StandardsParameters}
import org.carbonateresearch.conus.oldies.OldModelResults

final case class CalculateBurialDepthFromAgeModel(ageModel:List[(Double, Double)]) extends Calculator with StandardsParameters {

  override def outputs = List(Depth)
  override val inputs = Option(List(Age))

   override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {

    InterpolateValues(inputValueLabel = Age, output =Depth, xyList = ageModel).calculate(step, previousResults)

  }

}
