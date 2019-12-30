package org.carbonateresearch.conus.calculationparameters

import spire.math.Number
import spire.implicits._
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsParameters}

final case class CalculateBurialTemperatureFromGeothermalGradient(geothermalGradientsAgeMap: List[(Number,Number)]) extends CalculationStepValue with StandardsParameters{
  override def outputs = List(BurialTemperature)
  override def inputs = Option(List(Depth,GeothermalGradient))

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
    val burialTemperatures = previousResults(step)(Depth)*previousResults(step)(GeothermalGradient)/1000+previousResults(step)(SurfaceTemperature)

      Map(step -> Map(BurialTemperature -> burialTemperatures))

  }

}


