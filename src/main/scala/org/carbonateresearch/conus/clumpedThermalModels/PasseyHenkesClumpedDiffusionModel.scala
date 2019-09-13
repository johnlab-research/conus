package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
import spire.math.{Number, abs}

trait PasseyHenkesClumpedDiffusionModel {
  def tref = 699.7
  def kref = 0.00000292
  def ea = 197
  def r = 0.008314

  case object dT extends CalculationParametersIOLabels
  case object TKelvin extends CalculationParametersIOLabels
  case object D47i extends CalculationParametersIOLabels
  case object D47eq extends CalculationParametersIOLabels
  case object SampleTemp extends CalculationParametersIOLabels
  case object TemperatureDepth extends CalculationParametersIOLabels

  val D47eqFun = (t:Number) => Number(0.04028 * math.pow(10,6) / math.pow((t+273.15).toDouble,2) + 0.23776)
  val dTFun = (previousT: Number, currentT: Number) => abs(previousT-currentT)* 1000000 * 365 * 24 * 60 * 60
  val D47iFun =  (D47iStart: Number, D47eq: Number, T: Number,dTi: Number) =>
    (D47iStart - D47eq) * math.exp((-1*dTi * kref * math.exp((ea / r * ((1 / tref) - (1 / (T+273.15)))).toDouble)).toDouble) + D47eq
  val davies19_T: Number => Number = (D47: Number) => math.pow(0.04028 * math.pow(10,6) / (D47.toDouble - 0.23776),0.5)-273.15

}
