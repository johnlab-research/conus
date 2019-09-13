package org.carbonateresearch.conus.calculationparameters

import spire.math.Number
import spire.implicits._
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, StandardsIOLabels}

final case class BurialTemperatureCP(geothermalGradientsAgeMap: List[(Number,Number)]) extends CalculationParameters with StandardsIOLabels{

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
    val burialTemperatures = previousResults(step)(Depth)*previousResults(step)(GeothermalGradient)/1000+previousResults(step)(SurfaceTemperature)

      Map(step -> Map(BurialTemperature -> burialTemperatures))

  }

}


