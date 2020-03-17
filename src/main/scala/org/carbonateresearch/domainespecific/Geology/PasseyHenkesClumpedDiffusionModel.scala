package org.carbonateresearch.domainespecific.Geology

import org.carbonateresearch.conus.calculationparameters.parametersIO.SimulationVariable
import org.carbonateresearch.conus.util.StepFunctions._
import org.carbonateresearch.domainespecific.Geology.GeneralGeology._
import scala.math.{abs, exp, pow}

object PasseyHenkesClumpedDiffusionModel {
  def tref = 699.7
  def kref = 0.00000292
  def ea = 197
  def r = 0.008314

  val dT:SimulationVariable[Double] = SimulationVariable("dT","˚C",precision = 1)
  val TKelvin:SimulationVariable[Double] = SimulationVariable("T","˚K", precision = 1)
  val D47i:SimulationVariable[Double] = SimulationVariable("Δ47i"," ‰", precision = 3)
  val D47eq:SimulationVariable[Double] = SimulationVariable("Δ47eq"," ‰", precision = 3)
  val SampleTemp:SimulationVariable[Double] = SimulationVariable("Sample temperature","˚C", precision = 1)
  val TemperatureDepth:SimulationVariable[Double] = SimulationVariable("Temperature Depth","˚C", precision = 1)

  val D47eqFun: Step => Double = (s:Step) => 0.04028 * pow(10,6) / pow(BurialTemperature(s) + 273.15,2) + 0.23776
  val dTFun: Step => Double = (s:Step) => abs(BurialTemperature(s)-BurialTemperature(s-1))* 1000000 * 365 * 24 * 60 * 60
  val D47iFun: Step => Double =  (s:Step) =>
    (D47i(0) - D47eq(s)) * math.exp((-1*dT(s) * kref * exp((ea / r * ((1 / tref) - (1 / (BurialTemperature(s)+273.15))))))) + D47eq(s)
  val davies19_T: Step => Double = (s:Step) => pow(0.04028 * pow(10,6) / (D47iFun(s) - 0.23776),0.5)-273.15

}
