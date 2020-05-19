package org.carbonateresearch.conus.equations.parametersIO

import org.carbonateresearch.conus.common.ModelVariable

trait StandardsParameters {
val Depth = ModelVariable("Burial depth"," m", precision = 1)
val Age  = ModelVariable("Age"," Ma", precision = 3)
val SurfaceTemperature  = ModelVariable("Surface Temperature","˚C", precision =1)
val BurialTemperature = ModelVariable("Burial Temperature","˚C", precision =1)
val GeothermalGradient = ModelVariable("Geothermal gradient","˚C/km", precision =1)
}
