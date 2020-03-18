package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.SimulationVariable
import org.carbonateresearch.conus.util.StepFunctionUtils.Step

case class SingleCalculationDescription[T](f:Step=>T, saveAs:SimulationVariable[T]) {
  def as[A](f:Step => A):A = {
  println(f(0))
    f(0)}
}
