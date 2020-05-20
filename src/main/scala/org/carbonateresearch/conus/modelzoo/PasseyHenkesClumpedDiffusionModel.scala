package org.carbonateresearch.conus.modelzoo

import org.carbonateresearch.conus.common._
import GeneralGeology._
import scala.math.{abs, exp, pow}

object PasseyHenkesClumpedDiffusionModel {
  def tref = 699.7
  def kref = 0.00000292
  def ea = 197
  def r = 0.008314

  val dT:ModelVariable[Double] = ModelVariable("dT",0,"˚C",silent=true, precision = 1)
  val TKelvin:ModelVariable[Double] = ModelVariable("T",0,"˚K", precision = 1)
  val D47i:ModelVariable[Double] = ModelVariable("Δ47i",0.731," ‰", precision = 3)
  val D47eq:ModelVariable[Double] = ModelVariable("Δ47eq",0," ‰", precision = 3)
  val SampleTemp:ModelVariable[Double] = ModelVariable("Sample temperature",0,"˚C", precision = 1)
  val TemperatureDepth:ModelVariable[Double] = ModelVariable("Temperature Depth",0,"˚C", precision = 1)
  val initialAge:ModelVariable[Double] = ModelVariable("Initial age",110.0,"Ma")
  val finalAge:ModelVariable[Double] = ModelVariable("Final age",0.0,"Ma")

  val D47eqFun: Step => Double = (s:Step) => 0.04028 * pow(10,6) / pow(burialTemperature(s) + 273.15,2) + 0.23776
  val dTFun: Step => Double = (s:Step) => abs(burialTemperature(s)-burialTemperature(s-1))* 1000000 * 365 * 24 * 60 * 60
  val D47iFun: Step => Double =  (s:Step) =>
    (D47i(s-1) - D47eq(s)) * math.exp((-1*dT(s) * kref * exp((ea / r * ((1 / tref) - (1 / (burialTemperature(s)+273.15))))))) + D47eq(s)
  val davies19_T: Step => Double = (s:Step) => pow(0.04028 * pow(10,6) / (D47iFun(s) - 0.23776),0.5)-273.15


}
