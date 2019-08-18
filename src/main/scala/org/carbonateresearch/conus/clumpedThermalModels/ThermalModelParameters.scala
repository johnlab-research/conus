package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.calculationparameters.CalculationParameters

case class ThermalModelParameters (ageStep:Double, burialHistory: List[(Double,Double)], geothermalGradient: List[(Double,Double)], surfaceTemp: List[(Double, Double)])
  extends CalculationParameters
