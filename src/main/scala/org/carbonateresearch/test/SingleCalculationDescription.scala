package org.carbonateresearch.test
import RunTest.Step

case class SingleCalculationDescription[T](f:Step=>T,saveAs:Parameter[T]) {
  def as[A](f:Step => A):A = {
  println(f(0))
    f(0)}
}
