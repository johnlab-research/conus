package org.carbonateresearch.conus.equations

import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.oldies.OldModelResults

final case class SurfaceTemperaturesThroughTime(SurfaceTemperatureAgeMap:List[(Double, Double)]) extends Calculator with StandardsParameters {

  override val outputs: List[CalculationParametersIOLabels] = List(SurfaceTemperature)

  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {

    InterpolateValues(inputValueLabel = Age, output = SurfaceTemperature, xyList = SurfaceTemperatureAgeMap).calculate(step,previousResults)

  }

}
