package org.carbonateresearch.test
import org.carbonateresearch.conus.util.StepFunctionUtils.Step

case class SingleCalculationDescription[T](f:Step=>T,saveAs:ModelVariable[T]) {
  def as[A](f:Step => A):A = {
  println(f(0))
    f(0)}
}
