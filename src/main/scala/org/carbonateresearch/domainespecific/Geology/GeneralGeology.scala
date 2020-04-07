package org.carbonateresearch.domainespecific.Geology

import org.carbonateresearch.conus.equations.parametersIO.SimulationVariable

object GeneralGeology {
  val Depth = SimulationVariable[Double]("Burial depth"," m", precision = 1)
  val Age  = SimulationVariable[Double]("Age"," Ma", precision = 3)
  val SurfaceTemperature  = SimulationVariable[Double]("Surface Temperature","˚C", precision =1)
  val BurialTemperature = SimulationVariable[Double]("Burial Temperature","˚C", precision =1)
  val GeothermalGradient = SimulationVariable[Double]("Geothermal gradient","˚C/km", precision =1)
}
