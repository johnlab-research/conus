package org.carbonateresearch.conus.equations
import org.carbonateresearch.conus.equations.parametersIO.SimulationVariable
import org.carbonateresearch.conus.common.Step

case class SingleCalculationDescription[T](f:Step=>T, saveAs:SimulationVariable[T])
