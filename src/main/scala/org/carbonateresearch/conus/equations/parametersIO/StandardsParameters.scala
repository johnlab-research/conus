package org.carbonateresearch.conus.equations.parametersIO

trait StandardsParameters {
val Depth = SimulationVariable("Burial depth"," m", precision = 1)
val Age  = SimulationVariable("Age"," Ma", precision = 3)
val SurfaceTemperature  = SimulationVariable("Surface Temperature","˚C", precision =1)
val BurialTemperature = SimulationVariable("Burial Temperature","˚C", precision =1)
val GeothermalGradient = SimulationVariable("Geothermal gradient","˚C/km", precision =1)
}
