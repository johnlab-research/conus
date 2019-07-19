package org.carbonateresearch.diagenesims.clumpedThermalModels

import org.carbonateresearch.diagenesims.calculationparameters.CalculationParameters

case class ThermalModelParameters (ageStep:Double, burialHistory: List[(Double,Double)], geothermalGradient: List[(Double,Double)], surfaceTemp: List[(Double, Double)])
  extends CalculationParameters
