package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.calculationparameters.parametersIO.Parameter
import scala.math._


trait PasseyHenkesClumpedDiffusionModel {
  def tref = 699.7
  def kref = 0.00000292
  def ea = 197
  def r = 0.008314

  val dT:Parameter = Parameter("dT","˚C",precision = 1)
  val TKelvin:Parameter = Parameter("T","˚K", precision = 1)
  val D47i:Parameter = Parameter("Δ47i"," ‰", precision = 3)
  val D47eq:Parameter = Parameter("Δ47eq"," ‰", precision = 3)
  val SampleTemp:Parameter = Parameter("Sample temperature","˚C", precision = 1)
  val TemperatureDepth:Parameter = Parameter("Temperature Depth","˚C", precision = 1)

  val D47eqFun: Double => Double = (t:Double) => 0.04028 * pow(10,6) / pow(t+273.15,2) + 0.23776
  val dTFun: (Double,Double) => Double = (previousT: Double, currentT: Double) => abs(previousT-currentT)* 1000000 * 365 * 24 * 60 * 60
  val D47iFun: (Double, Double, Double, Double) => Double =  (D47iStart: Double, D47eq: Double, T: Double,dTi: Double) =>
    (D47iStart - D47eq) * math.exp((-1*dTi * kref * exp((ea / r * ((1 / tref) - (1 / (T+273.15))))))) + D47eq
  val davies19_T: Double => Double = (D47: Double) => pow(0.04028 * pow(10,6) / (D47 - 0.23776),0.5)-273.15

}
