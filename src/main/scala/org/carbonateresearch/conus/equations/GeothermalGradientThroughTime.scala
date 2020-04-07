package org.carbonateresearch.conus.equations

import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.oldies.OldModelResults


final case class GeothermalGradientThroughTime(geothermalGradientsAgeMap:List[(Double, Double)]) extends Calculator with StandardsParameters{

  override val outputs  = List(GeothermalGradient)
  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {

    InterpolateValues(inputValueLabel = Age, output = GeothermalGradient, xyList = geothermalGradientsAgeMap).calculate(step, previousResults)

  }

}
