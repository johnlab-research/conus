package org.carbonateresearch.conus.calculationparameters.parametersIO

trait StandardsParameters {
val Depth = Parameter("Burial depth"," m", precision = 1)
val Age  = Parameter("Age"," Ma", precision = 3)
val SurfaceTemperature  = Parameter("Surface Temperature","˚C", precision =1)
val BurialTemperature = Parameter("Burial Temperature","˚C", precision =1)
val GeothermalGradient = Parameter("Geothermal gradient","˚C/km", precision =1)
}
