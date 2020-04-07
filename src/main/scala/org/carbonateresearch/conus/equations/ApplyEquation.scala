package org.carbonateresearch.conus.equations
import org.carbonateresearch.conus.common.Step
import org.carbonateresearch.conus.equations.parametersIO.SimulationVariable

case class ApplyEquation[T](equation:Step=>T){

  def saveAs(saveLabel:SimulationVariable[T]): SingleCalculationDescription[T] = SingleCalculationDescription(equation,saveLabel)
}


