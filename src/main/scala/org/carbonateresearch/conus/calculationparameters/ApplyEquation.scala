package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.util.StepFunctionUtils.Step
import org.carbonateresearch.conus.calculationparameters.parametersIO.SimulationVariable

case class ApplyEquation[T](equation:Step=>T){

  def saveAs(saveLabel:SimulationVariable[T]): SingleCalculationDescription[T] = SingleCalculationDescription(equation,saveLabel)
}


