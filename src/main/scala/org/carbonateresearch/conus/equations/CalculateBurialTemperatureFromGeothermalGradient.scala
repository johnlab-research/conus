package org.carbonateresearch.conus.equations

import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, StandardsParameters}
import org.carbonateresearch.conus.common.SingleStepResults
import org.carbonateresearch.conus.oldies.OldModelResults

final case class CalculateBurialTemperatureFromGeothermalGradient(geothermalGradientsAgeMap: List[(Double,Double)]) extends Calculator with StandardsParameters{
  override def outputs = List(BurialTemperature)
  override def inputs = Option(List(Depth,GeothermalGradient))

  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {
    val myDepth:Double = previousResults.getStepResult(step,Depth)
    val myGeothermalGradient:Double = previousResults.getStepResult(step, GeothermalGradient)/1000
    val mySurfaceTemp:Double = previousResults.getStepResult(step,SurfaceTemperature)
    val burialTemperatures:Double = myDepth*myGeothermalGradient+mySurfaceTemp

    previousResults.addParameterResultAtStep(BurialTemperature,burialTemperatures,step)
  }

}


