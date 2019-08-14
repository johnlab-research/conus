package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number}
import spire.implicits._
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO.{GeothermalGradient, Depth, BurialTemperature, SurfaceTemperature, CalculationParametersIOLabels}

final case class BurialTemperatureCP(geothermalGradientsAgeMap: List[(Number,Number)]) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
    val burialTemperatures = previousResults(step)(Depth)*previousResults(step)(GeothermalGradient)/1000+previousResults(step)(SurfaceTemperature)

      Map(step -> Map(BurialTemperature -> burialTemperatures))

  }

}


