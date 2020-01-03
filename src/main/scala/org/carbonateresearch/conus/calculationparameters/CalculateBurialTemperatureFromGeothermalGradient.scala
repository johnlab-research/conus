package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.common.{ModelResults, SingleStepResults}

final case class CalculateBurialTemperatureFromGeothermalGradient(geothermalGradientsAgeMap: List[(Double,Double)]) extends CalculationStepValue with StandardsParameters{
  override def outputs = List(BurialTemperature)
  override def inputs = Option(List(Depth,GeothermalGradient))

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {
    val burialTemperatures:Double = previousResults.resultsForStep(step).valueForLabel(Depth)*previousResults.resultsForStep(step).valueForLabel(GeothermalGradient)/1000+previousResults.resultsForStep(step).valueForLabel(SurfaceTemperature)

    //ModelResults().newModelResultWithLabel(BurialTemperature).forValue(burialTemperatures).atStep(step)
    previousResults.addParameterResultAtStep(BurialTemperature,burialTemperatures,step)

  }

}


