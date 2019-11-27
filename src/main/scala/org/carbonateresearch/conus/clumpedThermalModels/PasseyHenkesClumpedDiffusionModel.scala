package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels,Parameter}
import spire.math.{Number, abs}

trait PasseyHenkesClumpedDiffusionModel {
  def tref = 699.7
  def kref = 0.00000292
  def ea = 197
  def r = 0.008314

  val dT = Parameter("dT","˚C",precision = 1)
  val TKelvin = Parameter("T","˚K", precision = 1)
  val D47i = Parameter("Δ47i"," ‰", precision = 3)
  val D47eq = Parameter("Δ47eq"," ‰", precision = 3)
  val SampleTemp = Parameter("Sample temperature","˚C", precision = 1)
  val TemperatureDepth = Parameter("Temperature Depth","˚C", precision = 1)

  val D47eqFun = (t:Number) => Number(0.04028 * math.pow(10,6) / math.pow((t+273.15).toDouble,2) + 0.23776)
  val dTFun = (previousT: Number, currentT: Number) => abs(previousT-currentT)* 1000000 * 365 * 24 * 60 * 60
  val D47iFun =  (D47iStart: Number, D47eq: Number, T: Number,dTi: Number) =>
    (D47iStart - D47eq) * math.exp((-1*dTi * kref * math.exp((ea / r * ((1 / tref) - (1 / (T+273.15)))).toDouble)).toDouble) + D47eq
  val davies19_T: Number => Number = (D47: Number) => math.pow(0.04028 * math.pow(10,6) / (D47.toDouble - 0.23776),0.5)-273.15

}
