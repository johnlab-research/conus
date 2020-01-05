package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.common.{ModelResults, SingleStepResults}

final case class CalculateBurialTemperatureFromGeothermalGradient(geothermalGradientsAgeMap: List[(Double,Double)]) extends Calculator with StandardsParameters{
  override def outputs = List(BurialTemperature)
  override def inputs = Option(List(Depth,GeothermalGradient))

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {
    val myDepth:Double = previousResults.getStepResult(step,Depth)
    val myGeothermalGradient:Double = previousResults.getStepResult(step, GeothermalGradient)/1000
    val mySurfaceTemp:Double = previousResults.getStepResult(step,SurfaceTemperature)
    val burialTemperatures:Double = myDepth*myGeothermalGradient+mySurfaceTemp

    previousResults.addParameterResultAtStep(BurialTemperature,burialTemperatures,step)
  }

}


